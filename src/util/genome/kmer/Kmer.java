package util.genome.kmer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.*;

public class Kmer implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private static final String KMER_REGEX = "^[AaCcGgTt]+\\s+[AaCcGgTt]+\\s+[\\-0123456789\\.]+\\s+[\\-0123456789\\.]+\\s+[\\-0123456789\\.]+$";
	
	public static Kmer readKmer(InputStream in){
		Map <String, Score> words = new LinkedHashMap<String, Score>();
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String line;
		try {
			while((line = reader.readLine()) != null){
				if(line.matches(KMER_REGEX)){
					String[] tokens = line.split("\\s+");
					Score s = new Score(Double.parseDouble(tokens[2]), Double.parseDouble(tokens[3]), Double.parseDouble(tokens[4]));
					words.put(tokens[0], s);
					words.put(tokens[1], s);
				}
			}
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return new Kmer(words);
	}
	
	private static class Score{
		public final double escore;
		public final double median;
		public final double zscore;
		
		public Score(double escore, double median, double zscore){
			this.escore = escore;
			this.median = median;
			this.zscore = zscore;
		}
	}
	
	private final Map<String, Score> m_Words;
	private final int m_WordLength;

	protected Kmer(Map<String, Score> words){
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
			Score s = m_Words.get(word);
			return s.escore;
		}
		throw new RuntimeException("Does not contain word "+word);
	}
	
	/**
	 * Returns an array containing the escore of each kmer (where k is the word length of this kmer object) of the given sequence
	 * @param sequence - sequence to be scored
	 * @return an array of escores
	 */
	public double[] escoreSequence(String sequence){
		if(sequence.length() < m_WordLength){
			throw new RuntimeException("Cannot score sequence shorter than word length");
		}
		double[] scores = new double[sequence.length() - m_WordLength + 1];
		for(int i=0; i<scores.length; i++){
			String cur = sequence.substring(i, i+m_WordLength);
			scores[i] = this.escore(cur);
		}
		return scores;
	}
	
	/**
	 * Returns the median value of the given word
	 * @param word
	 * @return
	 */
	public double median(String word){
		if(m_Words.containsKey(word)){
			Score s = m_Words.get(word);
			return s.median;
		}
		throw new RuntimeException("Does not contain word "+word);
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
			if(s.escore >= escore){
				words.put(word, s);
			}
		}
		return new Kmer(words);
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
			if(s.escore <= escore){
				words.put(word, s);
			}
		}
		return new Kmer(words);
	}
	
	
	
	
	
	
}

