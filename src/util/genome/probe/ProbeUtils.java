package util.genome.probe;

import util.DNAUtils;
import util.genome.GenomicCoordinate;
import util.genome.GenomicRegion;
import util.genome.GenomicSequence;
import util.genome.NoSuchBaseException;
import util.genome.Strand;
import util.genome.kmer.Kmer;
import util.genome.kmer.NoSuchWordException;
import util.genome.pwm.PWM;
import util.progress.ProgressEvent;
import util.progress.ProgressListener;
import util.progress.ProgressEvent.Type;

import java.util.*;

public class ProbeUtils {
	
	public static final Collection<Character> DNA_BASES = getDNABases();
			
	private static Collection<Character> getDNABases(){
		Collection<Character> bases = new HashSet<Character>();
		bases.add('A');
		bases.add('C');
		bases.add('G');
		bases.add('T');
		return bases;
	}
	
	public static interface Filter{
		public boolean keep(Probe p);
	}
	
	public static ProbeGroup filter(ProbeGroup probes, Filter f){
		List<Probe> filtered = new ArrayList<Probe>();
		for(Probe p : probes){
			if(f.keep(p)){
				filtered.add(p);
			}
		}
		return new ProbeGroup(filtered);
	}
	
	public static ProbeGroup joinProbes(ProgressListener l, Iterable<Probe> givenProbes, int bindingSites, int minDist, int maxDist){
		List<Probe> probes = new ArrayList<Probe>();
		for(Probe p : givenProbes){
			probes.add(p);
		}
		Collections.sort(probes);
		Queue<Probe> joinedProbes = new PriorityQueue<Probe>();
		int percentComplete = fireJoinProbesProgress(l, 0, probes.size(), -1);
		//for sub list of size=bindingSites in the list of probes
		for(int i=0; i<probes.size(); i++){
			Probe combined = join(probes, i, bindingSites, minDist, maxDist);
			if(combined != null){
				joinedProbes.add(combined);
			}
			percentComplete = fireJoinProbesProgress(l, i+1, probes.size(), percentComplete);
		}
		if(l != null){
			l.update(new ProgressEvent(null, Type.COMPLETED, "Done joining probes."));
		}
	
		
		return new ProbeGroup(joinedProbes);
	}
	
	protected static int fireJoinProbesProgress(ProgressListener l, int progress, int maxProgress, int prevPercent){
		if(l == null) return 0;
		int percent = progress*100/maxProgress;
		if(percent != prevPercent){
			l.update(new ProgressEvent(null, Type.UPDATE, progress, maxProgress, "Joining probes..."));
		}
		return percent;
	}
	
	public static ProbeGroup joinProbes(ProgressListener l, Iterable<Probe> givenProbes, int bindingSites, int minDist, int maxDist, int probeLen){
		List<Probe> probes = new ArrayList<Probe>();
		for(Probe p : givenProbes){
			probes.add(p);
		}
		Collections.sort(probes);
		Queue<Probe> joinedProbes = new PriorityQueue<Probe>();
		int percentComplete = fireJoinProbesProgress(l, 0, probes.size(), -1);
		//for sub list of size=bindingSites in the list of probes
		for(int i=0; i<probes.size(); i++){
			Probe combined = join(probes, i, bindingSites, minDist, maxDist, probeLen);
			if(combined != null){
				joinedProbes.add(combined);
			}
			percentComplete = fireJoinProbesProgress(l, i+1, probes.size(), percentComplete);
		}
		if(l != null){
			l.update(new ProgressEvent(null, Type.COMPLETED, "Done joining probes."));
		}
		
		return new ProbeGroup(joinedProbes);
		
	}
	
	public static Probe join(List<Probe> probes, int start, int bindingSites, int minDist, int maxDist, int probeLen){
		Probe combined = join(probes, start, bindingSites, minDist, maxDist);
		if(combined != null){
			GenomicRegion bindingRegion = GenomicRegion.union(combined.getBindingSites());
			if(bindingRegion.getSize() > probeLen){ //do not create probe if the binding region will not fit
				return null;
			}
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
	
	//private static long scorePWM;
	//private static long scoreKmer;
	
	public static List<Probe> extractFrom(
			GenomicSequence seq,
			Strand strand,
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
					//check which orientation the probe should be
					boolean reverse = reverseOrientation(seq.subsequence(center), pwm);
					//create a probe centered on the center region
					Probe p = createProbe(seq, center, strand, name + "_probe" + (probes.size() + 1), probeLength, reverse);
					//pwm.canScore() ensures that the probe only contains bases recognized
					//by the pwm
					if(p != null && !probes.contains(p) && pwm.canScore(p.getSequence())){
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
	
	private static boolean reverseOrientation(GenomicSequence seq, PWM pwm){
		return pwm.score(seq.getSequence().toUpperCase()) < pwm.score(DNAUtils.reverseCompliment(seq.getSequence().toUpperCase()));
	}
	
	private static Probe createProbe(GenomicSequence seq, GenomicRegion center, Strand strand, String name, int probeLength, boolean reverse){
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
		if(reverse){
			probeSeq = probeSeq.reverseCompliment();
			strand = Strand.reverse(strand);
		}
		return new Probe(probeSeq, new GenomicRegion[]{center}, name, strand);
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
			try{
				double score = score(curSeq, pwm);
				//System.err.println("Score = "+score);
				if(score > bestScore){
					bestScore = score;
					best = cur;
				}
			} catch (NoSuchBaseException e){
				//catch the NoSuchBaseException in case the sequence contains characters not recognized by
				//the pwm
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
		if(scores.length < 1){
			return false;
		}
		for(double d : scores){
			if(d < threshhold) return false;
		}
		return true;
	}
	
	private static double[] score(GenomicSequence seq, Kmer kmer){
		//long start = System.currentTimeMillis();
		try{
			double[] s = kmer.escoreSequence(seq.getSequence().toUpperCase());
			//scoreKmer += System.currentTimeMillis() - start;
			return s;
		} catch (NoSuchWordException e){
			return new double[]{};
		}
	}
	
	public static Probe mutate(ProgressListener l, Probe p, Kmer kmer, Set<Character> alphabet, int bindingSiteBarrier, double escoreCutoff, double maxOverlap){
		return Mutate.mutate(l, p, kmer, escoreCutoff, bindingSiteBarrier, maxOverlap, alphabet);
	}
	
	public static Probe mutate(ProgressListener l, Probe p, Kmer kmer, Set<Character> alphabet, int bindingSiteBarrier, double escoreCutoff, double maxOverlap, String primer){
		return Mutate.mutate(l, p, kmer, escoreCutoff, bindingSiteBarrier, maxOverlap, alphabet, primer);
	}

	public static List<Probe> generateBindingSitePermuations(Probe p){
		List<Probe> permutedProbes = new ArrayList<Probe>();
		GenomicRegion[] bindingSites = p.getBindingSites();
		List<List<GenomicRegion>> permutations = permutations(bindingSites);
		for(int i=0; i<permutations.size(); i++){
			permutedProbes.add(new Probe(p, permutations.get(i), p.getName() + "_t"+i));
		}
		return permutedProbes;
	}
	
	private static List<List<GenomicRegion>> permutations(GenomicRegion[] bindingSites){
		return permutations(new ArrayList<GenomicRegion>(), 0, bindingSites);
	}
	
	private static List<List<GenomicRegion>> permutations(List<GenomicRegion> prefix, int index, GenomicRegion[] bindingSites){
		List<List<GenomicRegion>> permutes = new ArrayList<List<GenomicRegion>>();
		if(index == bindingSites.length){ //there are no binding sites left, so return a list containing the prefix permutation
			permutes.add(prefix);
			return permutes;
		}
		//first get all permutations with the index binding site added
		List<GenomicRegion> newPrefix = new ArrayList<GenomicRegion>(prefix);
		newPrefix.add(bindingSites[index]);
		permutes.addAll(permutations(newPrefix, index+1, bindingSites));
		//now all permutations without the index binding site
		permutes.addAll(permutations(prefix, index+1, bindingSites));
		
		return permutes;
	}
	
	
	
	
}