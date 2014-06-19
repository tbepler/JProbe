package util.genome.kmer;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.*;
import java.util.Map.Entry;

import util.DNAUtils;
import util.Dictionary;
import util.ArrayUtils;

public class GappedKmer implements Kmer, Externalizable{
	private static final long serialVersionUID = 1L;
	
	private final Dictionary<String, Score> m_Words = new Dictionary<String, Score>('.');
	private List<String> m_WordList;
	private int[] m_MotifLens;
	
	/**
	 * Default constructor for deserialization purposes only.
	 */
	public GappedKmer(){
		//default constructor for deserialization purposes
	}
	
	GappedKmer(Map<String, Score> words, List<String> wordList){
		m_WordList = wordList;
		Set<Integer> sizes = new HashSet<Integer>();
		for(Entry<String,Score> e : words.entrySet()){
			String word = e.getKey();
			sizes.add(word.length());
			m_Words.put(word, e.getValue());
		}
		m_MotifLens = new int[sizes.size()];
		int i = 0;
		for(int len : sizes){
			m_MotifLens[i] = len;
			i++;
		}
		Arrays.sort(m_MotifLens);
	}
	
	/**
	 * Custom serialization for better efficiency
	 */
	@Override
	public void writeExternal(ObjectOutput out) throws IOException{
		//write the number of entries
		out.writeInt(m_WordList.size());
		//write each entry
		for(String word : m_WordList){ 
			out.writeObject(word);
			Score s = getScore(word);
			out.writeDouble(s.ESCORE);
			out.writeDouble(s.INTENSITY);
			out.writeDouble(s.ZSCORE);
		}
	}
	
	/**
	 * Custom deserialization
	 * @param in
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException{
		//retrieve the number of entries
		int entries = in.readInt();
		m_WordList = new ArrayList<String>(entries);
		Set<Integer> lens = new HashSet<Integer>();
		//read each entry
		while(--entries >= 0){
			String word = (String) in.readObject();
			lens.add(word.length());
			double escore = in.readDouble();
			double intensity = in.readDouble();
			double zscore = in.readDouble();
			Score s = new Score(escore, intensity, zscore);
			m_WordList.add(word);
			m_Words.put(word, s);
			m_Words.put(DNAUtils.reverseCompliment(word), s);
		}
		//initialize the length array
		m_MotifLens = new int[lens.size()];
		int i = 0;
		for(int len : lens){
			m_MotifLens[i++] = len;
		}
		Arrays.sort(m_MotifLens);
	}
	
	@Override
	public int size(){
		return m_WordList.size();
	}
	
	/**
	 * Converts the given word into a regex by appending "^" at the start and "$" at the end
	 * @param word
	 * @return
	 */
	protected static String toRegex(String word){
		return "^"+word+"$";
	}
	
	/**
	 * Checks whether the given word matches the given motif as a regex
	 * @param word
	 * @param motif
	 * @return
	 */
	protected static boolean matches(String word, String motif){
		return word.matches(toRegex(motif));
	}

	@Override
	public boolean contains(String word) {
		return m_Words.contains(word);
	}

	@Override
	public int[] getWordLengths() {
		return m_MotifLens;
	}
	
	/**
	 * Returns a collection containing the scores for motifs matching the given word
	 * @param word
	 * @return
	 */
	protected Collection<Score> getScores(String word){
		if(!m_Words.contains(word)){
			throw new NoSuchWordException("Kmer does not contain word: "+word+".");
		}
		List<Score> scores = m_Words.get(word);
		return scores;
		
	}
	
	@Override
	public double escore(String word) {
		Collection<Score> scores = this.getScores(word);
		return maxEScore(scores);
	}
	
	@Override 
	public double escore(String seq, int start, int end){
		return this.escore(seq.substring(start, end)); 
	}
	
	protected static double maxEScore(Collection<Score> scores){
		double escore = Double.NEGATIVE_INFINITY;
		for(Score s : scores){
			if(s.ESCORE > escore){
				escore = s.ESCORE;
			}
		}
		return escore;
	}
	
	protected static double maxIntensity(Collection<Score> scores){
		double inten = Double.NEGATIVE_INFINITY;
		for(Score s : scores){
			if(s.INTENSITY > inten){
				inten = s.INTENSITY;
			}
		}
		return inten;
	}
	
	protected static double maxZScore(Collection<Score> scores){
		double zscore = Double.NEGATIVE_INFINITY;
		for(Score s : scores){
			if(s.ZSCORE > zscore){
				zscore = s.ZSCORE;
			}
		}
		return zscore;
	}

	@Override
	public double[] escoreSequence(String sequence) {
		int[] lens = this.getWordLengths();
		int min = ArrayUtils.min(lens);
		if(sequence.length() < min){
			throw new RuntimeException("Cannot escore sequence: "+sequence+". Sequence length is less than min word length: "+min+".");
		}
		double[] scores = new double[sequence.length()];
		//initialize scores to -infinity
		Arrays.fill(scores, Double.NEGATIVE_INFINITY);
		//scan the sequence with each valid motif length
		for(int motifLen : lens){
			//check that word length is valid for this sequence
			if(sequence.length() >= motifLen){
				//score the word at each position
				for(int i=0; i<sequence.length() - motifLen + 1; i++){
					String word = sequence.substring(i, i+motifLen);
					double escore = this.escore(word);
					//assign each base within the word the score of the highest scoring word that contains it 
					for(int j=i; j<i+motifLen; j++){
						if(scores[j] < escore){
							scores[j] = escore;
						}
					}
				}
			}
		}
		return scores;
	}
	
	@Override
	public double[] escoreSequence(String sequence, int start, int end){
		return this.escoreSequence(sequence.substring(start, end));
	}

	@Override
	public double intensity(String word) {
		return maxIntensity(this.getScores(word));
	}

	@Override
	public double[] intensitySequence(String sequence) {
		int[] lens = this.getWordLengths();
		int min = ArrayUtils.min(lens);
		if(sequence.length() < min){
			throw new RuntimeException("Cannot intensity score sequence: "+sequence+". Sequence length is less than min word length: "+min+".");
		}
		double[] scores = new double[sequence.length()];
		//initialize scores to -infinity
		Arrays.fill(scores, Double.NEGATIVE_INFINITY);
		//scan the sequence with each valid motif length
		for(int motifLen : lens){
			//check that word length is valid for this sequence
			if(sequence.length() >= motifLen){
				//score the word at each position
				for(int i=0; i<sequence.length() - motifLen + 1; i++){
					String word = sequence.substring(i, i+motifLen);
					double intensity = this.intensity(word);
					//assign each base within the word the score of the highest scoring word that contains it 
					for(int j=i; j<i+motifLen; j++){
						if(scores[j] < intensity){
							scores[j] = intensity;
						}
					}
				}
			}
		}
		return scores;
	}

	@Override
	public double zscore(String word) {
		return maxZScore(this.getScores(word));
	}

	@Override
	public double[] zscoreSequence(String sequence) {
		int[] lens = this.getWordLengths();
		int min = ArrayUtils.min(lens);
		if(sequence.length() < min){
			throw new RuntimeException("Cannot zscore sequence: "+sequence+". Sequence length is less than min word length: "+min+".");
		}
		double[] scores = new double[sequence.length()];
		//initialize scores to -infinity
		Arrays.fill(scores, Double.NEGATIVE_INFINITY);
		//scan the sequence with each valid motif length
		for(int motifLen : lens){
			//check that word length is valid for this sequence
			if(sequence.length() >= motifLen){
				//score the word at each position
				for(int i=0; i<sequence.length() - motifLen + 1; i++){
					String word = sequence.substring(i, i+motifLen);
					double zscore = this.zscore(word);
					//assign each base within the word the score of the highest scoring word that contains it 
					for(int j=i; j<i+motifLen; j++){
						if(scores[j] < zscore){
							scores[j] = zscore;
						}
					}
				}
			}
		}
		return scores;
	}

	@Override
	public Iterator<String> iterator() {
		return m_WordList.iterator();
	}

	@Override
	public Score getScore(String word) {
		Collection<Score> scores = this.getScores(word);
		return new Score(GappedKmer.maxEScore(scores), GappedKmer.maxIntensity(scores), GappedKmer.maxZScore(scores));
		
	}

	@Override
	public String getWord(int index) {
		return m_WordList.get(index);
	}

}
