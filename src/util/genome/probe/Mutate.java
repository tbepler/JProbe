package util.genome.probe;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import util.DNAUtils;
import util.genome.GenomicCoordinate;
import util.genome.GenomicRegion;
import util.genome.GenomicSequence;
import util.genome.Strand;
import util.genome.kmer.Kmer;
import util.progress.ProgressListener;

class Mutate {
	
	private static class Mutation{
		public final GenomicCoordinate coord;
		public final char base;
		private final int hash;
		public Mutation(GenomicCoordinate coord, char base){
			this.coord = coord; this.base = base;
			this.hash = new HashCodeBuilder(17, 3).append(coord).append(base).toHashCode();
		}
		@Override
		public boolean equals(Object o){
			if(o == null) return false;
			if(o == this) return true;
			if(o instanceof Mutation){
				Mutation m = (Mutation) o;
				return this.coord.equals(m.coord) && this.base == m.base;
			}
			return false;
		}
		@Override
		public int hashCode(){
			return this.hash;
		}
	}
	
	private static class MutationRecord{
		public GenomicSequence seq = null;
		public Collection<Mutation> muts = new ArrayList<Mutation>();
	}
	
	public static Probe mutate(
			ProgressListener l,
			Probe mut,
			Kmer kmer,
			double escoreCutoff,
			int bindingSiteBarrier,
			double overlap,
			Set<Character> alphabet
			){
		
		List<GenomicRegion> bindingSites = getBindingSites(mut);
		List<GenomicRegion> protectedRegions = getProtectedRegions(bindingSites, bindingSiteBarrier);
		Set<GenomicCoordinate> immutableCoords = toCoordinateSet(protectedRegions);
		
		MutationRecord record = new MutationRecord();
		record.seq = mut.getStrand() != Strand.MINUS ? mut.asGenomicSequence() : mut.asGenomicSequence().reverseCompliment();
		//only use the binding sites as protected regions, but prevent mutations in binding site barrier
		record = mutateRecursive(l, record, kmer, bindingSites, immutableCoords, escoreCutoff, overlap, alphabet);
		
		List<GenomicCoordinate> mutations = new ArrayList<GenomicCoordinate>(mut.getMutations());
		for(Mutation m : record.muts){
			mutations.add(m.coord);
		}
		
		GenomicSequence mutatedSeq = mut.getStrand() != Strand.MINUS ? record.seq : record.seq.reverseCompliment();
		return new Probe(mut, mutatedSeq, mutations, !mutations.isEmpty());
		
	}
	
	public static Probe mutate(
			ProgressListener l,
			Probe p,
			Kmer kmer,
			double escoreCutoff,
			int bindingSiteBarrier,
			double overlap,
			Set<Character> alphabet,
			String primer
			){
		
		List<GenomicRegion> bindingSites = getBindingSites(p);
		
		List<GenomicRegion> protectedRegions = getProtectedRegions(bindingSites, bindingSiteBarrier);
		Set<GenomicCoordinate> immutableCoords = toCoordinateSet(protectedRegions);
		
		GenomicSequence genomicSeq = p.getStrand() != Strand.MINUS ? p.asGenomicSequence() : p.asGenomicSequence().reverseCompliment();
		
		//mutate probe sequence + primer
		String fwdSeq = genomicSeq.getSequence() + primer;
		GenomicRegion fwdRegion = new GenomicRegion(p.getRegion().getStart(), p.getRegion().getEnd().increment(primer.length()));
		GenomicSequence fwdGenomicSeq = new GenomicSequence(fwdSeq, fwdRegion);
		//add the primer region to the immutable coordinates
		Set<GenomicCoordinate> fwdImmutable = new HashSet<GenomicCoordinate>(immutableCoords);
		for(GenomicCoordinate coord = p.getRegion().getEnd().increment(1); coord.compareTo(fwdRegion.getEnd()) <= 0; coord = coord.increment(1)){
			fwdImmutable.add(coord);
		}
		MutationRecord record = new MutationRecord();
		record.seq = fwdGenomicSeq;
		//only use the binding sites as protected regions, but prevent mutations in binding site barrier
		record = mutateRecursive(l, record, kmer, bindingSites, fwdImmutable, escoreCutoff, overlap, alphabet);
		
		//add the fwd mutations to the mutation map - this guarantees only one mutation per coordinate
		Map<GenomicCoordinate, Mutation> mutations = new HashMap<GenomicCoordinate, Mutation>();
		for(Mutation m : record.muts){
			mutations.put(m.coord, m);
		}
		
		//mutate reverse comp primer + probe sequence - this is the reverse compliment probe + primerr
		String rvsSeq = DNAUtils.reverseCompliment(primer) + genomicSeq.getSequence();
		GenomicRegion rvsRegion = new GenomicRegion(p.getRegion().getStart().decrement(primer.length()), p.getRegion().getEnd());
		GenomicSequence rvsGenomicSeq = new GenomicSequence(rvsSeq, rvsRegion);
		//add the primer region to the immutable coordinates
		Set<GenomicCoordinate> rvsImmutable = new HashSet<GenomicCoordinate>(immutableCoords);
		for(GenomicCoordinate coord = rvsRegion.getStart(); coord.compareTo(p.getRegion().getStart()) < 0; coord = coord.increment(1)){
			rvsImmutable.add(coord);
		}
		record = new MutationRecord();
		record.seq = rvsGenomicSeq;
		record = mutateRecursive(l, record, kmer, bindingSites, rvsImmutable, escoreCutoff, overlap, alphabet);
		
		//add the rvs mutations to the mutation map
		for(Mutation m : record.muts){
			mutations.put(m.coord, m);
		}
		
		//make mutations and store them in a list
		List<GenomicCoordinate> mutationList = new ArrayList<GenomicCoordinate>();
		GenomicSequence mutated = genomicSeq;
		for(Mutation m : mutations.values()){
			if(mutated.contains(m.coord)){
				mutated = makeMutation(m, mutated);
				mutationList.add(m.coord);
			}
		}
		
		if(p.getStrand() == Strand.MINUS){
			mutated = mutated.reverseCompliment();
		}
		
		return new Probe(p, mutated, mutationList, !mutationList.isEmpty());
		
	}
	
	private static MutationRecord mutateRecursive(
			ProgressListener l,
			MutationRecord record,
			Kmer kmer,
			List<GenomicRegion> protectedRegions,
			Set<GenomicCoordinate> immutable,
			double escoreCutoff,
			double overlap,
			Set<Character> alphabet
			){
		
		GenomicSequence seq = record.seq;
		Map<GenomicSequence, Double> scores = score(seq, kmer, protectedRegions, immutable, overlap);
		
		if(allScoresBelowCutoff(scores, escoreCutoff)){
			return record;
		}
		
		GenomicSequence mutate = pickHighestScoringSubseq(scores);
		
		Mutation mut = pickBestMutation(mutate, kmer, immutable, alphabet); //picks the mutation that lowers the score the most
		if(mut == null){ //no mutation could lower the score
			immutable.addAll(toCoordinateSet(mutate.getRegion()));
			return mutateRecursive(l, record, kmer, protectedRegions, immutable, escoreCutoff, overlap, alphabet);
		}else{ //make best mutation
			GenomicSequence mutatedSeq = makeMutation(mut, seq);
			record.seq = mutatedSeq;
			record.muts.add(mut);
			immutable.add(mut.coord);
			return mutateRecursive(l, record, kmer, protectedRegions, immutable, escoreCutoff, overlap, alphabet);
		}
	}
	
	private static Mutation pickBestMutation(GenomicSequence seq, Kmer kmer, Set<GenomicCoordinate> immutable, Set<Character> alphabet){
		Mutation best = null;
		double bestScore = kmer.escore(seq.getSequence());
		for(GenomicCoordinate coord : seq){
			if(!immutable.contains(coord)){
				for(Mutation mut : possibleMutations(seq, coord, alphabet)){
					GenomicSequence result = makeMutation(mut, seq);
					double resultScore = kmer.escore(result.getSequence());
					if(resultScore < bestScore){
						best = mut;
						bestScore = resultScore;
					}
				}
			}
		}
		return best;
	}
	
	private static GenomicSequence makeMutation(Mutation mut, GenomicSequence seq){
		return seq.mutate(mut.coord, mut.base);
	}
	
	private static List<Mutation> possibleMutations(GenomicSequence seq, GenomicCoordinate location, Set<Character> alphabet){
		List<Mutation> muts = new ArrayList<Mutation>();
		char cur = seq.getBaseAt(location);
		for(char c : alphabet){
			if(c != cur){
				muts.add(new Mutation(location, c));
			}
		}
		return muts;
	}
	
	private static GenomicSequence pickHighestScoringSubseq(Map<GenomicSequence, Double> scores){
		GenomicSequence seq = null;
		double score = Double.NEGATIVE_INFINITY;
		for(Entry<GenomicSequence, Double> e : scores.entrySet()){
			if(e.getValue() > score){
				seq = e.getKey();
				score = e.getValue();
			}
		}
		return seq;
	}
	
	private static boolean allScoresBelowCutoff(Map<GenomicSequence, Double> scores, double cutoff){
		for(Double score : scores.values()){
			if(score > cutoff) return false;
		}
		return true;
	}
	
	private static Map<GenomicSequence, Double> score(
			GenomicSequence seq,
			Kmer kmer,
			List<GenomicRegion> protectedRegions,
			Set<GenomicCoordinate> immutable,
			double maxOverlap
			){
		
		Map<GenomicSequence, Double> scores = new HashMap<GenomicSequence, Double>();
		for(int wordLen : kmer.getWordLengths()){
			for(GenomicSequence subseq : subseqs(seq, wordLen)){
				if(meetsOverlapCriteria(subseq, protectedRegions, maxOverlap) && isMutable(subseq, immutable)){
					scores.put(subseq, kmer.escore(subseq.getSequence()));
				}
			}
		}
		return scores;
	}
	
	private static boolean isMutable(GenomicSequence seq, Set<GenomicCoordinate> immutable){
		for(GenomicCoordinate coord : seq){
			if(!immutable.contains(coord)) return true;
		}
		return false;
	}
	
	private static boolean meetsOverlapCriteria(GenomicSequence subseq, List<GenomicRegion> protectedRegions, double maxOverlap){
		for(GenomicRegion r : protectedRegions){
			double overlap = (double) subseq.getOverlap(r) / (double) subseq.length();
			if(overlap > maxOverlap){
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
