package util.genome.kmer;

import java.util.*;

public class UngappedKmer implements Kmer{
	private static final long serialVersionUID = 1L;
	
	private final Map<String, Score> m_Words;
	private final int m_WordLength;

	UngappedKmer(Map<String, Score> words){
		m_Words = words;

		int length = 0;
		for(String word : m_Words.keySet()){
			length = word.length();
			break;
		}
		m_WordLength = length;
	}
	
	public int getWordLength(){
		return m_WordLength;
	}
	
	public boolean contains(String word){
		return m_Words.containsKey(word);
	}
	
	/**
	 * Returns the escore value of the given word
	 * @param word
	 * @return
	 */
	public double escore(String word){
		if(m_Words.containsKey(word)){
			return m_Words.get(word).ESCORE;
		}
		throw new RuntimeException("Does not contain word "+word);
	}
	
	@Override
	public double escore(String seq, int start, int end){
		return this.escore(seq.substring(start, end));
	}
	
	@Override
	public double[] escoreSequence(String sequence){
		if(sequence.length() < m_WordLength){
			throw new RuntimeException("Cannot escore sequence: "+sequence+". Sequence is shorter than kmer word length: "+m_WordLength+".");
		}
		double[] scores = new double[sequence.length()];
		//initialize scores to -infinity
		for(int i=0; i<scores.length; i++){
			scores[i] = Double.NEGATIVE_INFINITY;
		}
		//score each word and assign that score to each contained base if it is greater than
		//the score currently assigned to that base
		for(int i=0; i<this.numWords(sequence); i++){
			String word = this.wordAt(sequence, i);
			Double escore = this.escore(word);
			for(int j=i; j<i+m_WordLength; j++){
				if(scores[j] < escore){
					scores[j] = escore;
				}
			}
		}
		return scores;
	}
	
	@Override
	public double[] escoreSequence(String sequence, int start, int end){
		int len = end - start;
		if(len < m_WordLength){
			throw new RuntimeException("Cannot escore sequence: "+sequence+" between "+start+" and "+end+". "+len+" is shorter than kmer word length: "+m_WordLength+".");
		}
		double[] scores = new double[len];
		//initialize scores to -infinity
		for(int i=0; i<scores.length; i++){
			scores[i] = Double.NEGATIVE_INFINITY;
		}
		//score each word and assign that score to each contained base if it is greater than
		//the score currently assigned to that base
		for(int i=0; i<len-m_WordLength+1; i++){
			Double escore = this.escore(sequence, start+i, start+i+m_WordLength);
			for(int j=i; j<i+m_WordLength; j++){
				if(scores[j] < escore){
					scores[j] = escore;
				}
			}
		}
		return scores;
	}
	
	/**
	 * Returns a new Kmer containing words from this kmer with escores above or equal to the given escore.
	 * @param escore - minimum escore of words in kmer returned
	 * @return new kmer containing words with escores above or equal to the specified escore
	 */
	public Kmer filterAboveEScore(double escore){
		Map<String, Score> words = new LinkedHashMap<String, Score>();
		for(String word : m_Words.keySet()){
			Score s = m_Words.get(word);
			if(s.ESCORE >= escore){
				words.put(word, s);
			}
		}
		return new UngappedKmer(words);
	}
	
	/**
	 * Returns a new Kmer containing words from this kmer with escores less than or equal to the given escore.
	 * @param escore - maximum escore of words in the kmer returned
	 * @return new kmer containing words with escores less than or equal tothe specified escore
	 */
	public Kmer filterBelowEScore(double escore){
		Map<String, Score> words = new LinkedHashMap<String, Score>();
		for(String word : m_Words.keySet()){
			Score s = m_Words.get(word);
			if(s.ESCORE <= escore){
				words.put(word, s);
			}
		}
		return new UngappedKmer(words);
	}

	@Override
	public int[] getWordLengths() {
		return new int[]{m_WordLength};
	}
	
	protected int numWords(String seq){
		return seq.length() - m_WordLength + 1;
	}
	
	protected String wordAt(String seq, int index){
		return seq.substring(index, index + m_WordLength);
	}

	@Override
	public double intensity(String word) {
		if(m_Words.containsKey(word)){
			return m_Words.get(word).INTENSITY;
		}
		throw new RuntimeException("Cannot find intensity for word: "+word+". Word is not contained by this kmer.");
	}

	@Override
	public double[] intensitySequence(String sequence) {
		if(sequence.length() < m_WordLength){
			throw new RuntimeException("Cannot intensity score sequence: "+sequence+". Sequence is shorter than kmer word length: "+m_WordLength+".");
		}
		double[] scores = new double[sequence.length()];
		//initialize scores to -infinity
		for(int i=0; i<scores.length; i++){
			scores[i] = Double.NEGATIVE_INFINITY;
		}
		//score each word and assign that score to each contained base if it is greater than 
		//that base's currently assigned score
		for(int i=0; i< this.numWords(sequence); i++){
			String word = this.wordAt(sequence, i);
			double intensity = this.intensity(word);
			for(int j=i; j<i+m_WordLength; j++){
				if(scores[j] < intensity){
					scores[j] = intensity;
				}
			}
		}
		return scores;
	}

	@Override
	public double zscore(String word) {
		if(m_Words.containsKey(word)){
			return m_Words.get(word).ZSCORE;
		}
		throw new RuntimeException("Cannot find zscore for word: "+word+". Word is not contained by this kmer.");
	}

	@Override
	public double[] zscoreSequence(String sequence) {
		if(sequence.length() < m_WordLength){
			throw new RuntimeException("Cannot zscore sequence: "+sequence+". Sequence is shorter than kmer word length: "+m_WordLength+".");
		}
		double[] scores = new double[sequence.length()];
		//initialize scores to -infinity
		for(int i=0; i<scores.length; i++){
			scores[i] = Double.NEGATIVE_INFINITY;
		}
		//score each word and assign that score to each contained base if it is
		//greater than the current score of that base
		for(int i=0; i<this.numWords(sequence); i++){
			String word = this.wordAt(sequence, i);
			double zscore = this.zscore(word);
			for(int j=i; j<i+m_WordLength; j++){
				if(scores[j] < zscore){
					scores[j] = zscore;
				}
			}
		}
		return scores;
	}
	
	
	
	
	
	
}

