package chiptools.jprobe.function.mutationprofiler;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import jprobe.services.data.Data;
import jprobe.services.function.Argument;
import util.genome.kmer.Kmer;
import util.genome.kmer.Kmers;
import util.progress.ProgressListener;
import chiptools.jprobe.data.GenericTable;
import chiptools.jprobe.function.AbstractChiptoolsFunction;

public class MutationProfiler extends AbstractChiptoolsFunction<MutationProfilerParams>{

	protected MutationProfiler() {
		super(MutationProfilerParams.class);
	}

	@Override
	public Collection<Argument<? super MutationProfilerParams>> getArguments() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Data execute(ProgressListener l, MutationProfilerParams params) throws Exception {
		GenericTable mutProfiles = new GenericTable();
		for(File f : params.kmerLibrary){
			if(f.exists() && f.canRead()){
				Kmer kmer = Kmers.readKmer(new FileInputStream(f));
				profile(kmer, f.getName(), params.seq1, params.seq1Name, params.seq2, params.seq2Name, mutProfiles);
			}
		}
		return mutProfiles;
	}
	
	protected void profile(Kmer kmer, String kmerName, String seq1, String seq1Name, String seq2, String seq2Name, GenericTable data){
		data.appendRow(kmerName);
		List<Integer> mutations = findMutationIndexes(seq1, seq2);
		for(int mut : mutations){
			Map<String, String> subseqMap = getSubseqsOverIndex(kmer, seq1, seq2, mut);
			Scores scores = getLargestScoreDif(kmer, subseqMap);
			
			//put seq1 score into table
			String col = "pos"+mut+"_"+seq1Name+"_score";
			if(!data.containsCol(col)){
				data.appendCol(col);
			}
			data.put(col, kmerName, scores.seq1Score);
			
			//put seq2 score into table
			col = "pos"+mut+"_"+seq2Name+"_score";
			if(!data.containsCol(col)){
				data.appendCol(col);
			}
			data.put(col, kmerName, scores.seq2Score);
			
			//put score difference into table
			col = "pos"+mut+"_dif";
			if(!data.containsCol(col)){
				data.appendCol(col);
			}
			data.put(col, kmerName, Math.abs(scores.seq1Score - scores.seq2Score));
		}
	}
	
	protected Scores getLargestScoreDif(Kmer kmer, Map<String, String> subseqMap){
		Scores best = null;
		double bestDif = Double.NEGATIVE_INFINITY;
		for(Entry<String, String> e : subseqMap.entrySet()){
			String seq1 = e.getKey();
			String seq2 = e.getValue();
			double score1 = kmer.escore(seq1);
			double score2 = kmer.escore(seq2);
			double dif = Math.abs(score1 - score2);
			if(dif > bestDif){
				best = new Scores(score1, score2);
				bestDif = dif;
			}
		}
		return best;
	}
	
	protected static class Scores{
		public final double seq1Score;
		public final double seq2Score;

		public Scores(double seq1, double seq2){
			seq1Score = seq1; seq2Score = seq2;
		}
	}
	
	protected Map<String, String> getSubseqsOverIndex(Kmer kmer, String seq1, String seq2, int index){
		Map<String, String> seqMap = new HashMap<String, String>();
		int seqLen = Math.min(seq1.length(), seq2.length());
		for(int len : kmer.getWordLengths()){
			int firstIndex = Math.max(0, index - len + 1);
			int lastIndex = Math.min(seqLen - len, index);
			for(int i=firstIndex; i<=lastIndex; i++){
				String seq1subseq = seq1.substring(i, i+len);
				String seq2subseq = seq2.substring(i, i+len);
				seqMap.put(seq1subseq, seq2subseq);
			}
		}
		return seqMap;
	}
	
	protected List<Integer> findMutationIndexes(String seq1, String seq2){
		List<Integer> muts = new ArrayList<Integer>();
		for(int i=0; i<seq1.length() && i<seq2.length(); i++){
			if(seq1.charAt(i) != seq2.charAt(i)){
				muts.add(i);
			}
		}
		return muts;
	}

}
