package util.genome.kmer;

import java.io.Serializable;

public interface Kmer extends Serializable{
	
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
	 * Returns the escore of the given word
	 * @param word
	 * @return
	 */
	public double escore(String word);
	
	/**
	 * Returns an array containing the highest escore that is associated with each nucleotide. That is
	 * the highest escore of any word scored by this kmer containing that nucleotide.
	 * @param sequence
	 * @return an array containing an escore for each nucleotide in the sequence
	 */
	public double[] escoreSequence(String sequence);
	
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
