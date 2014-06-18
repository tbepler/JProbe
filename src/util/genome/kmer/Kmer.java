package util.genome.kmer;

import java.io.Serializable;

import org.apache.commons.lang3.builder.HashCodeBuilder;

public interface Kmer extends Serializable, Iterable<String>{
	
	public static class Score implements Serializable{
		private static final long serialVersionUID = 1L;
		
		public final double ESCORE;
		public final double INTENSITY;
		public final double ZSCORE;
		private final int m_Hash;
		
		public Score(double escore, double intensity, double zscore){
			this.ESCORE = escore;
			this.INTENSITY = intensity;
			this.ZSCORE = zscore;
			m_Hash = new HashCodeBuilder(61, 101).append(ESCORE).append(INTENSITY).append(ZSCORE).toHashCode();
		}
		
		@Override
		public String toString(){
			return String.valueOf(ESCORE) + "\t" + String.valueOf(INTENSITY) + "\t" +String.valueOf(ZSCORE);
		}
		
		@Override
		public int hashCode(){
			return m_Hash;
		}
		
		@Override
		public boolean equals(Object o){
			if(o == null) return false;
			if(o == this) return true;
			if(o instanceof Score){
				Score other = (Score) o;
				return this.ESCORE == other.ESCORE && this.INTENSITY == other.INTENSITY && this.ZSCORE == other.ZSCORE;
			}
			return false;
		}
		
	}
	
	/**
	 * Returns the forward word stored at the given index.
	 * @param index - index of the word to retrieve
	 * @return
	 */
	public String getWord(int index);
	
	/**
	 * Returns the number of entries in this kmer.
	 * @return
	 */
	public int size();
	
	/**
	 * Tests whether the given word is scored by this kmer
	 * @param word - the word to test
	 * @return True if the word is scored by this kmer, False otherwise
	 */
	public boolean contains(String word);
	
	/**
	 * Returns an array containing the nucleotide lengths of the words scored by this kmer
	 * @return
	 */
	public int[] getWordLengths();
	
	/**
	 * Returns the Score object associated with the given word
	 * @param word
	 * @return
	 */
	public Score getScore(String word);
	
	/**
	 * Returns the escore of the given word
	 * @param word
	 * @return
	 */
	public double escore(String word);
	
	public double escore(String seq, int start, int end);
	
	/**
	 * Returns an array containing the highest escore that is associated with each nucleotide. That is
	 * the highest escore of any word scored by this kmer containing that nucleotide.
	 * @param sequence
	 * @return an array containing an escore for each nucleotide in the sequence
	 */
	public double[] escoreSequence(String sequence);
	
	public double[] escoreSequence(String seq, int start, int end);
	
	/**
	 * Returns the intensity of the given word
	 * @param word
	 * @return
	 */
	public double intensity(String word);
	
	/**
	 * Returns an array containing the highest intensity that is associated with each nucleotide.
	 * That is the highest intensity of any word scored by this kmer containing that nucleotide
	 * @param sequence
	 * @return
	 */
	public double[] intensitySequence(String sequence);
	
	/**
	 * Returns the zscore of the given word
	 * @param word
	 * @return
	 */
	public double zscore(String word);
	
	/**
	 * Returns an array containing the highest zscore that is associated with each nucleotide.
	 * That is the highest zscore of any worde scored by this kmer containing that nucleotide.
	 * @param sequence
	 * @return
	 */
	public double[] zscoreSequence(String sequence);
	
}
