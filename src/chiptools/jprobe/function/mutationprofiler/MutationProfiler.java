package chiptools.jprobe.function.mutationprofiler;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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
import chiptools.jprobe.data.MutationProfile;
import chiptools.jprobe.function.AbstractChiptoolsFunction;

public class MutationProfiler extends AbstractChiptoolsFunction<MutationProfilerParams>{

	public MutationProfiler() {
		super(MutationProfilerParams.class);
	}

	@Override
	public Collection<Argument<? super MutationProfilerParams>> getArguments() {
		Collection<Argument<? super MutationProfilerParams>> args = new ArrayList<Argument<? super MutationProfilerParams>>();
		args.add(new CompareSeqsArg(this, 2, false));
		args.add(new KmerLibraryArg(this, false));
		args.add(new MinEscoreArg(this, true));
		args.add(new MinDifferenceArg(this, true));
		args.add(new BindingSiteArg(this, true));
		args.add(new RecursiveArg(this));
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
		MutationProfile mutProfiles = new MutationProfile();
		List<File> kmers = new ArrayList<File>();
		this.extractChildrenFiles(params.kmerLibrary, kmers, params.recursive);
		for(File f : kmers){
			if(f.exists() && f.canRead()){
				try{
					Kmer kmer = Kmers.readKmer(new FileInputStream(f));
					if(kmer.size() > 0){
						l.update(new ProgressEvent(this, Type.UPDATE, "Processing Kmer "+f.getName(), true));
						profile(
								kmer,
								f.getName(),
								params.seq1,
								params.seq1Name,
								params.seq2,
								params.seq2Name,
								params.bindingSite,
								params.minEscore,
								params.minDifference,
								mutProfiles
								);
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
	
	protected static void profile(
			Kmer kmer,
			String kmerName,
			String seq1,
			String seq1Name,
			String seq2,
			String seq2Name,
			int bindingSite,
			double minEscore,
			double minDif,
			MutationProfile data
			){
		data.appendRow(kmerName);
		List<Integer> mutations = findMutationIndexes(seq1, seq2);
		for(int mut : mutations){
			List<Subsequences> subseqs = getSubseqsOverIndex(kmer, bindingSite, minEscore, seq1, seq2, mut);
			Scores scores = getLargestScoreDif(kmer, minDif, subseqs);
			
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
	
	protected static Scores getLargestScoreDif(Kmer kmer, double minDif, List<Subsequences> subseqsList){
		Scores best = null;
		double bestDif = minDif;
		for(Subsequences subseqs : subseqsList){
			String seq1 = subseqs.seq1;
			String seq2 = subseqs.seq2;
			double score1 = kmer.escore(seq1);
			double score2 = kmer.escore(seq2);
			double dif = Math.abs(score1 - score2);
			if(dif >= bestDif){
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
	
	protected static class Subsequences{
		public final String seq1;
		public final String seq2;
		
		public Subsequences(String seq1, String seq2){
			this.seq1 = seq1; this.seq2 = seq2;
		}
	}
	
	protected static List<Subsequences> getSubseqsOverIndex(Kmer kmer, int bindingSite, double minEscore, String seq1, String seq2, int index){
		List<Subsequences> list = new ArrayList<Subsequences>();
		int seqLen = Math.min(seq1.length(), seq2.length());
		for(int wordLen : kmer.getWordLengths()){
			int siteLen = bindingSite > 0 ? bindingSite : wordLen;
			if(siteLen < wordLen){
				continue;
			}
			int firstIndex = Math.max(0, index - siteLen + 1); 
			int lastIndex = Math.min(seqLen - siteLen, index);
			for(int start=firstIndex; start<=lastIndex; start++){
				int end = start + siteLen;
				if(end > seqLen){
					break;
				}
				//tile from start to end with words of length wordLen and check that each word exceeds the minEscore
				List<Subsequences> subseqs = new ArrayList<Subsequences>();
				for(int i=start; i<=end-wordLen; i++){
					String seq1subseq = seq1.substring(i, i+wordLen);
					String seq2subseq = seq2.substring(i, i+wordLen);
					//add the subseqs if they meet the escore criteria
					if(kmer.escore(seq1subseq) >= minEscore || kmer.escore(seq2subseq) >= minEscore){
						subseqs.add(new Subsequences(seq1subseq, seq2subseq));
					}else{ //escore criteria not met, so clear the subseqs and stop looking at this binding site
						subseqs.clear();
						break;
					}
				}
				list.addAll(subseqs);
			}
		}
		return list;
	}
	
	protected static List<Subsequences> getSubseqsOverIndex(Kmer kmer, int bindingSite, String seq1, String seq2, int index){
		return getSubseqsOverIndex(kmer, bindingSite, Double.NEGATIVE_INFINITY, seq1, seq2, index);
	}
	
	protected static List<Subsequences> getSubseqsOverIndex(Kmer kmer, double minEscore, String seq1, String seq2, int index){
		return getSubseqsOverIndex(kmer, -1, minEscore, seq1, seq2, index);
	}
	
	protected static List<Subsequences> getSubseqsOverIndex(Kmer kmer, String seq1, String seq2, int index){
		return getSubseqsOverIndex(kmer, Double.NEGATIVE_INFINITY, seq1, seq2, index);
	}
	
	protected static List<Integer> findMutationIndexes(String seq1, String seq2){
		List<Integer> muts = new ArrayList<Integer>();
		for(int i=0; i<seq1.length() && i<seq2.length(); i++){
			if(seq1.charAt(i) != seq2.charAt(i)){
				muts.add(i);
			}
		}
		return muts;
	}

}
