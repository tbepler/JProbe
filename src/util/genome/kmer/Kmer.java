package util.genome.kmer;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.*;

public class Kmer implements Serializable{
	private static final long serialVersionUID = 1L;
	
	public static Kmer readKmer(InputStream in){
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		
		
		return null;
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
	
}

