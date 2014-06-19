package util.genome.kmer;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.*;

import util.DNAUtils;

public class UngappedKmer implements Kmer, Externalizable{
	private static final long serialVersionUID = 1L;
	
	private Map<String, Score> m_Words;
	private List<String> m_WordList;
	private final Collection<Integer> m_WordLens = new HashSet<Integer>();

	UngappedKmer(Map<String, Score> words, List<String> wordList){
		m_Words = words;
		m_WordList = wordList;
		for(String word : m_Words.keySet()){
			m_WordLens.add(word.length());
		}
	}
	
	@Override
	public int size(){
		return m_WordList.size();
	}
	
	@Override
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
		throw new NoSuchWordException("Does not contain word "+word);
	}
	
	@Override
	public double escore(String seq, int start, int end){
		return this.escore(seq.substring(start, end));
	}
	
	private int minWordLen(){
		int min = Integer.MAX_VALUE;
		for(int len : m_WordLens){
			if(len < min) min = len;
		}
		return min;
	}
	
	@Override
	public double[] escoreSequence(String sequence){
		if(sequence.length() < this.minWordLen()){
			throw new RuntimeException("Cannot escore sequence: "+sequence+". Sequence is shorter than minimum kmer word length: "+this.minWordLen()+".");
		}
		double[] scores = new double[sequence.length()];
		//initialize scores to -infinity
		for(int i=0; i<scores.length; i++){
			scores[i] = Double.NEGATIVE_INFINITY;
		}
		//score each word and assign that score to each contained base if it is greater than
		//the score currently assigned to that base
		for(int i=0; i<this.maxIndex(sequence); i++){
			List<String> words = this.wordsAt(sequence, i);
			for(String word : words){
				Double escore = this.escore(word);
				for(int j=i; j<i+word.length(); j++){
					if(scores[j] < escore){
						scores[j] = escore;
					}
				}
			}
		}
		return scores;
	}
	
	@Override
	public double[] escoreSequence(String sequence, int start, int end){
		int len = end - start;
		if(len < this.minWordLen()){
			throw new RuntimeException("Cannot escore sequence: "+sequence+" between "+start+" and "+end+". "+len+" is shorter than minimum kmer word length: "+this.minWordLen()+".");
		}
		double[] scores = new double[len];
		//initialize scores to -infinity
		for(int i=0; i<scores.length; i++){
			scores[i] = Double.NEGATIVE_INFINITY;
		}
		//score each word and assign that score to each contained base if it is greater than
		//the score currently assigned to that base
		for(int i=0; i<len-this.minWordLen()+1; i++){
			List<String> words = this.wordsAt(sequence, start + i, end);
			for(String word : words){
				Double escore = this.escore(word);
				for(int j=i; j<i+word.length(); j++){
					if(scores[j] < escore){
						scores[j] = escore;
					}
				}
			}
		}
		return scores;
	}

	@Override
	public int[] getWordLengths() {
		int[] lens = new int[m_WordLens.size()];
		int i = 0;
		for(Integer len : m_WordLens){
			lens[i] = len;
			++i;
		}
		return lens;
	}
	
	protected int maxIndex(String seq){
		return seq.length() - this.minWordLen() + 1;
	}
	
	protected List<String> wordsAt(String seq, int index, int max){
		List<String> words = new ArrayList<String>();
		for(int len : m_WordLens){
			if(index + len <= max){
				words.add(seq.substring(index, index+len));
			}
		}
		return words;
	}
	
	protected List<String> wordsAt(String seq, int index){
		List<String> words = new ArrayList<String>();
		for(int len : m_WordLens){
			if(index + len <= seq.length()){
				words.add(seq.substring(index, index+len));
			}
		}
		return words;
	}

	@Override
	public double intensity(String word) {
		if(m_Words.containsKey(word)){
			return m_Words.get(word).INTENSITY;
		}
		throw new NoSuchWordException("Cannot find intensity for word: "+word+". Word is not contained by this kmer.");
	}

	@Override
	public double[] intensitySequence(String sequence) {
		if(sequence.length() < this.minWordLen()){
			throw new RuntimeException("Cannot intensity score sequence: "+sequence+". Sequence is shorter than kmer word length: "+this.minWordLen()+".");
		}
		double[] scores = new double[sequence.length()];
		//initialize scores to -infinity
		for(int i=0; i<scores.length; i++){
			scores[i] = Double.NEGATIVE_INFINITY;
		}
		//score each word and assign that score to each contained base if it is greater than 
		//that base's currently assigned score
		for(int i=0; i< this.maxIndex(sequence); i++){
			List<String> words = this.wordsAt(sequence, i);
			for(String word : words){
				double intensity = this.intensity(word);
				for(int j=i; j<i+word.length(); j++){
					if(scores[j] < intensity){
						scores[j] = intensity;
					}
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
		throw new NoSuchWordException("Cannot find zscore for word: "+word+". Word is not contained by this kmer.");
	}

	@Override
	public double[] zscoreSequence(String sequence) {
		if(sequence.length() < this.minWordLen()){
			throw new RuntimeException("Cannot zscore sequence: "+sequence+". Sequence is shorter than kmer word length: "+this.minWordLen()+".");
		}
		double[] scores = new double[sequence.length()];
		//initialize scores to -infinity
		for(int i=0; i<scores.length; i++){
			scores[i] = Double.NEGATIVE_INFINITY;
		}
		//score each word and assign that score to each contained base if it is
		//greater than the current score of that base
		for(int i=0; i<this.maxIndex(sequence); i++){
			List<String> words = this.wordsAt(sequence, i);
			for(String word : words){
				double zscore = this.zscore(word);
				for(int j=i; j<i+word.length(); j++){
					if(scores[j] < zscore){
						scores[j] = zscore;
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
		return m_Words.get(word);
	}

	@Override
	public String getWord(int index) {
		return m_WordList.get(index);
	}
	
	/**
	 * Custom deserialization
	 */
	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		//read number of entries
		int entries = in.readInt();
		m_WordList = new ArrayList<String>(entries);
		m_Words = new HashMap<String, Score>(entries*2);
		//read each entry
		while(--entries >= 0){
			String word = (String) in.readObject();
			m_WordLens.add(word.length());
			double escore = in.readDouble();
			double intensity = in.readDouble();
			double zscore = in.readDouble();
			Score s = new Score(escore, intensity, zscore);
			m_WordList.add(word);
			m_Words.put(word, s);
			m_Words.put(DNAUtils.reverseCompliment(word), s);
		}
	}
	
	/**
	 * Custom serialization for better space efficiency
	 */
	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		//write number of entries
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
	
	
	
	
	
	
}

