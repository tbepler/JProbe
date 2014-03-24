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
		return new Probe(revComp, p.getRegion(), p.getName(), revStrand, p.isMutant());
	}
	
	public static ProbeGroup joinProbes(Iterable<Probe> givenProbes, int bindingSites, int minDist, int maxDist, int siteWidth, int probeLen){
		List<Probe> probes = new ArrayList<Probe>();
		for(Probe p : givenProbes){
			probes.add(p);
		}
		Collections.sort(probes);
		Queue<Probe> joinedProbes = new PriorityQueue<Probe>();
		
		//for sub list of size=bindingSites in the list of probes
		for(int i=0; i<probes.size()-bindingSites+1; i++){
			int last = i+bindingSites;
			GenomicSequence combinedSeq = join(probes, i, last);
			if(combinedSeq != null){
				//TODO
			}
		}
		return null;
		
	}
	
	private static GenomicSequence join(List<Probe> probes, int start, int end){
		GenomicSequence combined = probes.get(start).asGenomicSequence();
		for(int i=start+1; i<end; i++){
			Probe nextProbe = probes.get(i);
			GenomicSequence next;
			if(nextProbe.getStrand() == Strand.MINUS){
				next = reverseCompliment(nextProbe).asGenomicSequence();
			}else{
				next = nextProbe.asGenomicSequence();
			}
			if(combined.adjacentTo(next) || combined.overlaps(next)){
				combined = combined.join(next);
			}else{
				return null;
			}
		}
		return combined;
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
		int count = 1;
		while(start.compareTo(stop) < 0){
			GenomicSequence bindingSite = seq.subsequence(start, start.increment(bindingSiteLength - 1));
			//check if the binding site meets the threshhold criteria
			if(meetsThreshhold(score(bindingSite, kmer), escoreThreshhold)){
				//scan window around binding site with the PWM for best scoring region
				GenomicRegion center = scanWindow(seq, bindingSite.getRegion(), pwm, windowSize);
				if(center != null){
					//create a probe centered on the center region
					Probe p = createProbe(seq, center, name + count, probeLength);
					if(p != null && !probes.contains(p)){
						probes.add(p);
						count++;
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
		return new Probe(probeSeq, name);
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
