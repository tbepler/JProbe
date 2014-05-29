package util.genome.probe;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import util.genome.GenomicCoordinate;
import util.genome.GenomicRegion;
import util.genome.GenomicSequence;
import util.genome.kmer.Kmer;
import util.progress.ProgressListener;

public class Mutate {
	
	private static class Mutation{
		public final GenomicCoordinate coord;
		public final char base;
		public Mutation(GenomicCoordinate coord, char base){
			this.coord = coord; this.base = base;
		}
	}
	
	private static class MutationRecord{
		public GenomicSequence seq = null;
		public Collection<Mutation> muts = new ArrayList<Mutation>();
	}
	
	public static Probe mutate(ProgressListener l, Probe mut, Kmer kmer, double escoreCutoff, int bindingSiteBarrier){
		List<GenomicRegion> bindingSites = getBindingSites(mut);
		List<GenomicRegion> protectedRegions = getProtectedRegions(bindingSites, bindingSiteBarrier);
		Set<GenomicCoordinate> immutableCoords = toCoordinateSet(protectedRegions);
		
		return null;
	}
	
	private static MutationRecord mutateRecursive(
			ProgressListener l,
			MutationRecord record,
			List<GenomicRegion> protectedRegions,
			Set<GenomicCoordinate> immutable,
			double escoreCutoff
			){
		
		GenomicSequence seq = record.seq;
		
		
		return null;
	}
	
	private static Map<GenomicSequence, Double> score(GenomicSequence seq, Kmer kmer, List<GenomicRegion> protectedRegions){
		
		for(int wordLen : kmer.getWordLengths()){
			for(GenomicSequence subseq : subseqs(seq, wordLen)){
				
			}
		}
		
		return null;
	}
	
	private static boolean shouldScore(GenomicSequence subseq, List<GenomicRegion> protectedRegions){
		int maxOverlap = subseq.length() / 2;
		for(GenomicRegion r : protectedRegions){
			if(maxOverlap < subseq.getOverlap(r)){
				return false;
			}
		}
		return true;
	}
	
	private static List<GenomicSequence> subseqs(GenomicSequence seq, int subseqLength){
		List<GenomicSequence> subseqs = new ArrayList<GenomicSequence>();
		GenomicCoordinate start = seq.getStart();
		GenomicCoordinate end = start.increment(subseqLength - 1);
		while(end.compareTo(seq.getEnd()) <= 0){
			subseqs.add(seq.subsequence(start, end));
			start = start.increment(1);
			end = end.increment(1);
		}
		return subseqs;
	}
	
	private static List<GenomicRegion> getBindingSites(Probe p){
		List<GenomicRegion> bindingSites = new ArrayList<GenomicRegion>();
		for(GenomicRegion bindingSite : p.getBindingSites()){
			bindingSites.add(bindingSite);
		}
		return bindingSites;
	}
	
	private static List<GenomicRegion> getProtectedRegions(List<GenomicRegion> bindingSites, int bindingSiteBarrier){
		List<GenomicRegion> prot = new ArrayList<GenomicRegion>();
		for(GenomicRegion bindingSite : bindingSites){
			GenomicRegion protRegion = new GenomicRegion(
					bindingSite.getStart().decrement(bindingSiteBarrier),
					bindingSite.getEnd().increment(bindingSiteBarrier)
					);
			prot.add(protRegion);
		}
		return prot;
	}
	
	private static Set<GenomicCoordinate> toCoordinateSet(Collection<GenomicRegion> regions){
		Set<GenomicCoordinate> coords = new HashSet<GenomicCoordinate>();
		for(GenomicRegion r : regions){
			for(GenomicCoordinate c : r){
				coords.add(c);
			}
		}
		return coords;
	}
	
	private static Set<GenomicCoordinate> toCoordinateSet(GenomicRegion region){
		Set<GenomicCoordinate> coords = new HashSet<GenomicCoordinate>();
		for(GenomicCoordinate coord : region){
			coords.add(coord);
		}
		return coords;
	}
	
}
