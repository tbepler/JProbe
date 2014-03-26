package util.genome.probe;

import util.DNAUtils;
import util.genome.GenomicCoordinate;
import util.genome.GenomicRegion;
import util.genome.GenomicSequence;
import util.genome.Strand;
import util.genome.kmer.Kmer;
import util.genome.pwm.PWM;

import java.util.*;

public class ProbeUtils {
	
	public static Probe reverseCompliment(Probe p){
		String revComp = DNAUtils.reverseCompliment(p.getSequence());
		Strand revStrand = Strand.reverse(p.getStrand());
		GenomicRegion[] bindingSites = p.getBindingSites();
		GenomicRegion[] mirrored = new GenomicRegion[bindingSites.length];
		for(int i=0; i<bindingSites.length; i++){
			mirrored[mirrored.length -1 - i] = bindingSites[i].mirror(p.getRegion());
		}
		return new Probe(revComp, p.getRegion(), mirrored, p.getName(), revStrand, p.isMutant());
	}
	
	public static ProbeGroup joinProbes(Iterable<Probe> givenProbes, int bindingSites, int minDist, int maxDist, int probeLen){
		List<Probe> probes = new ArrayList<Probe>();
		for(Probe p : givenProbes){
			probes.add(p);
		}
		Collections.sort(probes);
		Queue<Probe> joinedProbes = new PriorityQueue<Probe>();
		
		//for sub list of size=bindingSites in the list of probes
		for(int i=0; i<probes.size(); i++){
			Probe combined = join(probes, i, bindingSites, minDist, maxDist, probeLen);
			if(combined != null){
				joinedProbes.add(combined);
			}
		}
		
		return new ProbeGroup(joinedProbes);
		
	}
	
	public static Probe join(List<Probe> probes, int start, int bindingSites, int minDist, int maxDist, int probeLen){
		Probe combined = join(probes, start, bindingSites, minDist, maxDist);
		if(combined != null){
			GenomicRegion bindingRegion = GenomicRegion.union(combined.getBindingSites());
			int flank = probeLen - (int) bindingRegion.getSize();
			GenomicCoordinate regStart;
			GenomicCoordinate regEnd;
			//since decrement and increment work with negative numbers, this will correctly expand or reduce the
			//region to the desired probe size
			if(flank%2==0){
				regStart = bindingRegion.getStart().decrement(flank/2);
				regEnd = bindingRegion.getEnd().increment(flank/2);
			}else{
				//if flank odd, add +1 to left
				regStart = bindingRegion.getStart().decrement(flank/2 + 1);
				regEnd = bindingRegion.getEnd().increment(flank/2);
			}
			assert(new GenomicRegion(regStart, regEnd).getSize() == probeLen);
			Probe subprobe = combined.subprobe(new GenomicRegion(regStart, regEnd), combined.getName());
			if(subprobe.numBindingSites() == bindingSites){
				return subprobe;
			}
		}
		return null;
	}
	
	public static Probe join(List<Probe> probes, int start, int bindingSites, int minDist, int maxDist){
		Probe combined = join(probes, start, probes.size(), bindingSites);
		if(combined != null && meetsBindingCriteria(combined, minDist, maxDist)){
			return combined;
		}
		return null;
	}
	
	private static boolean meetsBindingCriteria(Probe probe, int minDist, int maxDist){
		GenomicRegion[] bindingSites = probe.getBindingSites();
		for(int i=0; i<bindingSites.length-1; i++){
			if(!meetsBindingCriteria(bindingSites[i], bindingSites[i+1], minDist, maxDist)){
				return false;
			}
		}
		return true;
	}
	
	private static boolean meetsBindingCriteria(GenomicRegion siteA, GenomicRegion siteB, int minDist, int maxDist){
		long dist = siteA.distance(siteB);
		return dist >= minDist && dist <= maxDist;
	}
	
	private static Probe join(List<Probe> probes, int start, int end, int bindingSites){
		Probe combined = probes.get(start);
		for(int i=start+1; i<end; i++){
			if(combined.numBindingSites() > bindingSites) return null;
			if(combined.numBindingSites() == bindingSites) return combined;
			Probe nextProbe = probes.get(i);
			if(combined.adjacentTo(nextProbe) || combined.overlaps(nextProbe)){
				combined = combined.combine(nextProbe, combineNames(combined, nextProbe));
			}else{
				return null;
			}
		}
		if(combined.numBindingSites() != bindingSites){
			return null;
		}
		return combined;
	}
	
	private static String combineNames(Probe a, Probe b){
		String x = a.getName();
		String y = b.getName();
		if(x.equals(y)){
			return x;
		}
		return x+"/"+y;
	}
	
	private static long scorePWM;
	private static long scoreKmer;
	
	public static List<Probe> extractFrom(
			GenomicSequence seq,
			String name,
			Kmer kmer,
			PWM pwm,
			int probeLength,
			int bindingSiteLength,
			int windowSize,
			double escoreThreshhold
			){
		
		Collection<Probe> probes = new LinkedHashSet<Probe>();
		//scorePWM = 0;
		//scoreKmer = 0;
		//iterate over each subsequence of size bindingSiteLength within seq
		GenomicCoordinate stop = seq.getEnd().decrement(bindingSiteLength - 2);
		GenomicCoordinate start = seq.getStart();
		while(start.compareTo(stop) < 0){
			GenomicSequence bindingSite = seq.subsequence(start, start.increment(bindingSiteLength - 1));
			//check if the binding site meets the threshhold criteria
			if(meetsThreshhold(score(bindingSite, kmer), escoreThreshhold)){
				//scan window around binding site with the PWM for best scoring region
				GenomicRegion center = scanWindow(seq, bindingSite.getRegion(), pwm, windowSize);
				if(center != null){
					//create a probe centered on the center region
					Probe p = createProbe(seq, center, name, probeLength);
					if(p != null && !probes.contains(p)){
						probes.add(p);
					}
				}
			}
			start = start.increment(1);
		}
		List<Probe> probeList = new ArrayList<Probe>(probes);
		//System.err.println("PWM Score time = "+scorePWM+", Kmer Score time = "+scoreKmer);
		//System.err.println(kmer.getClass());
		return probeList;
	}
	
	private static Probe createProbe(GenomicSequence seq, GenomicRegion center, String name, int probeLength){
		int flankLength = probeLength - (int) center.getSize();
		GenomicCoordinate start;
		GenomicCoordinate end;
		//if flanklength is odd, then take 1 base less from the right
		if(flankLength % 2 == 1){
			start = center.getStart().decrement(flankLength/2 + 1);
			end = center.getEnd().increment(flankLength/2);
		}else{
			start = center.getStart().decrement(flankLength/2);
			end = center.getEnd().increment(flankLength/2);
		}
		//make sure start and end are in bounds
		if(start.compareTo(seq.getStart()) < 0 || end.compareTo(seq.getEnd()) > 0){
			return null;
		}
		GenomicSequence probeSeq = seq.subsequence(start, end);
		return new Probe(probeSeq, new GenomicRegion[]{center}, name);
	}
	
	private static GenomicRegion scanWindow(GenomicSequence seq, GenomicRegion bindingSite, PWM pwm, int windowSize){
		GenomicCoordinate windowStart = bindingSite.getStart().decrement(windowSize);
		if(windowStart.compareTo(seq.getStart()) < 0) windowStart = seq.getStart();
		GenomicCoordinate windowEnd = bindingSite.getEnd().increment(windowSize);
		if(windowEnd.compareTo(seq.getEnd()) > 0) windowEnd = seq.getEnd();
		GenomicRegion window = new GenomicRegion(windowStart, windowEnd);
		if(window.getSize() < pwm.length()) return null;
		//score each region of size pwm.length() within the window region and keep the highest scoring region
		GenomicRegion best = null;
		double bestScore = Double.NEGATIVE_INFINITY;
		//System.err.println("PWM length: "+pwm.length());
		GenomicRegion cur = new GenomicRegion(window.getStart(), window.getStart().increment(pwm.length() - 1));
		while(cur.getEnd().compareTo(window.getEnd()) <= 0){
			GenomicSequence curSeq = seq.subsequence(cur);
			//System.err.println("Scoring: "+curSeq);
			double score = score(curSeq, pwm);
			//System.err.println("Score = "+score);
			if(score > bestScore){
				bestScore = score;
				best = cur;
			}
			cur = cur.increment(1);
		}
		return best;
	}
	
	private static double score(GenomicSequence seq, PWM pwm){
		//long start = System.currentTimeMillis();
		double s = Math.max(pwm.score(seq.getSequence().toUpperCase()), pwm.score(DNAUtils.reverseCompliment(seq.getSequence().toUpperCase())));
		//scorePWM += System.currentTimeMillis() - start;
		return s;
	}
	
	private static boolean meetsThreshhold(double[] scores, double threshhold){
		for(double d : scores){
			if(d < threshhold) return false;
		}
		return true;
	}
	
	private static double[] score(GenomicSequence seq, Kmer kmer){
		//long start = System.currentTimeMillis();
		double[] s = kmer.escoreSequence(seq.getSequence().toUpperCase());
		//scoreKmer += System.currentTimeMillis() - start;
		return s;
	}
	
	
	
	
	
	
	
	
	
	
}
