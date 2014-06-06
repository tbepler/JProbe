package chiptools.jprobe.function.mutationprofiler;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import jprobe.services.ErrorHandler;
import jprobe.services.Log;
import jprobe.services.data.Data;
import jprobe.services.function.Argument;
import util.genome.kmer.Kmer;
import util.genome.kmer.Kmers;
import util.progress.ProgressEvent;
import util.progress.ProgressListener;
import util.progress.ProgressEvent.Type;
import chiptools.jprobe.ChiptoolsActivator;
import chiptools.jprobe.data.GenericTable;
import chiptools.jprobe.data.MutationProfile;
import chiptools.jprobe.function.AbstractChiptoolsFunction;

public class MutationProfiler extends AbstractChiptoolsFunction<MutationProfilerParams>{

	public MutationProfiler() {
		super(MutationProfilerParams.class);
	}

	@Override
	public Collection<Argument<? super MutationProfilerParams>> getArguments() {
		Collection<Argument<? super MutationProfilerParams>> args = new ArrayList<Argument<? super MutationProfilerParams>>();
		args.add(new CompareSeqsArg(2, false));
		args.add(new KmerLibraryArg(false));
		args.add(new MinEscoreArg(true));
		args.add(new RecursiveArg());
		return args;
	}
	
	protected void extractChildrenFiles(File dir, List<File> fill, boolean recursive){
		if(dir.isDirectory()){
			for(File child : dir.listFiles()){
				if(recursive && child.isDirectory()){
					extractChildrenFiles(child, fill, recursive);
				}else if(!child.isDirectory() && child.canRead()){
					fill.add(child);
				}
			}
		}else{
			fill.add(dir);
		}
	}

	@Override
	public Data execute(ProgressListener l, MutationProfilerParams params) throws Exception {
		GenericTable mutProfiles = new MutationProfile();
		List<File> kmers = new ArrayList<File>();
		this.extractChildrenFiles(params.kmerLibrary, kmers, params.recursive);
		for(File f : kmers){
			if(f.exists() && f.canRead()){
				try{
					Kmer kmer = Kmers.readKmer(new FileInputStream(f));
					if(kmer.size() > 0){
						l.update(new ProgressEvent(this, Type.UPDATE, "Processing Kmer "+f.getName(), true));
						if(params.useMinEscore){
							profile(kmer, f.getName(), params.seq1, params.seq1Name, params.seq2, params.seq2Name, params.minEscore, mutProfiles);
						}else{
							profile(kmer, f.getName(), params.seq1, params.seq1Name, params.seq2, params.seq2Name, mutProfiles);
						}
					}else{
						l.update(new ProgressEvent(this, Type.UPDATE, "Skipping Kmer "+f.getName(), true));
						Log.getInstance().write(ChiptoolsActivator.getBundle(), "MutationProfilter: skipping "+f.getName());
					}
				}catch(Exception e){
					l.update(new ProgressEvent(this, Type.UPDATE, "Skipping Kmer "+f.getName(), true));
					Log.getInstance().write(ChiptoolsActivator.getBundle(), "MutationProfilter: skipping "+f.getName());
					ErrorHandler.getInstance().handleException(e, ChiptoolsActivator.getBundle());
				}
			}
		}
		return mutProfiles;
	}
	
	protected void profile(Kmer kmer, String kmerName, String seq1, String seq1Name, String seq2, String seq2Name, double minEscore, GenericTable data){
		data.appendRow(kmerName);
		List<Integer> mutations = findMutationIndexes(seq1, seq2);
		for(int mut : mutations){
			Map<String, String> subseqMap = getSubseqsOverIndex(kmer, minEscore, seq1, seq2, mut);
			Scores scores = getLargestScoreDif(kmer, subseqMap);
			
			//put seq1 score into table
			String col = "pos"+(mut+1)+"_"+seq1Name+"_score";
			if(!data.containsCol(col)){
				data.appendCol(col);
			}
			Object val;
			if(scores != null){
				val = scores.seq1Score;
			}else{
				val = new Double(-1);
			}
			data.put(col, kmerName, val);
			
			//put seq2 score into table
			col = "pos"+(mut+1)+"_"+seq2Name+"_score";
			if(!data.containsCol(col)){
				data.appendCol(col);
			}
			if(scores != null){
				val = scores.seq2Score;
			}else{
				val = new Double(-1);
			}
			data.put(col, kmerName, val);
			
			//put score difference into table
			col = "pos"+(mut+1)+"_dif";
			if(!data.containsCol(col)){
				data.appendCol(col);
			}
			if(scores != null){
				val = Math.abs(scores.seq1Score - scores.seq2Score);
			}else{
				val = new Double(-1);
			}
			data.put(col, kmerName, val);
		}
	}
	
	protected void profile(Kmer kmer, String kmerName, String seq1, String seq1Name, String seq2, String seq2Name, GenericTable data){
		data.appendRow(kmerName);
		List<Integer> mutations = findMutationIndexes(seq1, seq2);
		for(int mut : mutations){
			Map<String, String> subseqMap = getSubseqsOverIndex(kmer, seq1, seq2, mut);
			Scores scores = getLargestScoreDif(kmer, subseqMap);
			
			//put seq1 score into table
			String col = "pos"+(mut+1)+"_"+seq1Name+"_score";
			if(!data.containsCol(col)){
				data.appendCol(col);
			}
			data.put(col, kmerName, scores.seq1Score);
			
			//put seq2 score into table
			col = "pos"+(mut+1)+"_"+seq2Name+"_score";
			if(!data.containsCol(col)){
				data.appendCol(col);
			}
			data.put(col, kmerName, scores.seq2Score);
			
			//put score difference into table
			col = "pos"+(mut+1)+"_dif";
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
	
	protected Map<String, String> getSubseqsOverIndex(Kmer kmer, double minEscore, String seq1, String seq2, int index){
		Map<String, String> seqMap = new HashMap<String, String>();
		int seqLen = Math.min(seq1.length(), seq2.length());
		for(int len : kmer.getWordLengths()){
			int firstIndex = Math.max(0, index - len + 1);
			int lastIndex = Math.min(seqLen - len, index);
			for(int i=firstIndex; i<=lastIndex; i++){
				String seq1subseq = seq1.substring(i, i+len);
				String seq2subseq = seq2.substring(i, i+len);
				if(kmer.escore(seq1subseq) >= minEscore || kmer.escore(seq2subseq) >= minEscore){
					seqMap.put(seq1subseq, seq2subseq);
				}
			}
		}
		return seqMap;
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
