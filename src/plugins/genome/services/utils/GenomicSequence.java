package plugins.genome.services.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import org.apache.commons.lang3.builder.HashCodeBuilder;

public class GenomicSequence implements Serializable, Comparable<GenomicSequence>{
	private static final long serialVersionUID = 1L;
	
	private final String m_Sequence;
	private final GenomicRegion m_Region;
	private final int m_HashCode;
	
	public GenomicSequence(String sequence, GenomicRegion region){
		if(sequence.length() != region.getSize()){
			throw new RuntimeException("Error: sequence and region are of different sizes");
		}
		m_Sequence = sequence;
		m_Region = region;
		m_HashCode = this.computeHash();
	}
	
	private int computeHash(){
		return new HashCodeBuilder(439, 61).append(m_Region).append(m_Sequence).toHashCode();
	}
	
	GenomicContext getGenomicContext(){
		return m_Region.getGenomicContext();
	}
	
	public GenomicSequence join(GenomicSequence other){
		if(this.getGenomicContext() != other.getGenomicContext()){
			throw new RuntimeException("Error: cannot join sequences from different genomes "+this.getGenomicContext()+" and "+other.getGenomicContext());
		}
		if(!m_Region.adjacentTo(other.getRegion()) && !m_Region.overlaps(other.getRegion())){
			throw new RuntimeException("Error: cannot join two GenomicSequences that are not overlapping or adjacent");
		}
		GenomicSequence first = m_Region.compareTo(other.getRegion()) < 1 ? this : other;
		GenomicSequence second = first != this ? this : other;
		int overlap = (int) m_Region.getOverlap(other.getRegion());
		String seq = first.getSequence() + second.getSequence().substring(overlap);
		return new GenomicSequence(seq, m_Region.union(other.getRegion()));
	}
	
	private GenomicCoordinate getLocationAt(int index){
		Chromosome chr = null;
		int chrNotGreaterThanIndex = -1;
		for(Chromosome cur : m_ChrIndexes.keySet()){
			int curIndex = m_ChrIndexes.get(cur);
			if(curIndex > chrNotGreaterThanIndex && curIndex <= index){
				chr = cur;
				chrNotGreaterThanIndex = curIndex;
			}
		}
		if(chr.equals(this.getStart().getChromosome())){
			return new GenomicCoordinate(chr, index - chrNotGreaterThanIndex + this.getStart().getBaseIndex());
		}else{
			return new GenomicCoordinate(chr, index - chrNotGreaterThanIndex + 1);
		}
	}
	
	/**
	 * Splits this GenomicSequence into 2 GenomicSequences around the given GenomicLocation. The second sequence
	 * will start at the location, while the first sequence will end just before the given location.
	 * @param loc - location around which to split this GenomicSequence
	 * @return and array containing two GenomicSequences. The first being before the location and the second starting
	 * at the location
	 */
	public GenomicSequence[] split(GenomicCoordinate loc){
		this.checkLocation(loc);
		int index = getIndexOf(loc);
		GenomicCoordinate leftStart = this.getStart();
		GenomicCoordinate leftEnd = getLocationAt(index - 1);
		GenomicCoordinate rightStart = loc;
		GenomicCoordinate rightEnd = this.getEnd();
		String leftSeq = m_Sequence.substring(0, index);
		String rightSeq = m_Sequence.substring(index);
		GenomicSequence left = new GenomicSequence(leftSeq, new GenomicRegion(leftStart, leftEnd));
		GenomicSequence right = new GenomicSequence(rightSeq, new GenomicRegion(rightStart, rightEnd));
		return new GenomicSequence[]{left, right};
	}
	
	/**
	 * Tests whether the given GenomicLocation occurs within this GenomicSequence
	 * @param loc - GenomicLocation to test
	 * @param comparator - comparator to use for comparing GenomicLocations
	 * @return True if the given location occurs within this sequence, False otherwise
	 */
	public boolean contains(GenomicCoordinate loc, Comparator<GenomicCoordinate> comparator){
		return (loc != null && m_ChrIndexes.containsKey(loc.getChromosome()) && comparator.compare(this.getStart(), loc) <= 0 && comparator.compare(this.getEnd(), loc) >= 0);
	}
	
	/**
	 * Returns the length of this GenomicSequence
	 * @return
	 */
	public int length(){
		return m_Sequence.length();
	}
	
	/**
	 * Returns the starting location of this GenomicSequence
	 * @return a GenomicLocation representing the first position of this sequence
	 */
	public GenomicCoordinate getStart(){
		return m_Region.getStart();
	}
	
	/**
	 * Returns the ending location of this GenomicSequence
	 * @return a GenomicLocation representing the last position of this sequence
	 */
	public GenomicCoordinate getEnd(){
		return m_Region.getEnd();
	}
	
	private void checkLocation(GenomicCoordinate loc){
		if(loc == null){
			throw new RuntimeException("Error: genomic location may not be null");
		}
		if(loc.getBaseIndex() <= 0){
			throw new RuntimeException("Error: base index "+loc.getBaseIndex()+" is not a valid base index. Base indexes must be > 0");
		}
		if(!m_ChrIndexes.containsKey(loc.getChromosome())){
			throw new RuntimeException("Error: region does not contain chromosome "+loc.getChromosome());
		}
		if(loc.getChromosome().equals(this.getStart().getChromosome()) && loc.getBaseIndex() < this.getStart().getBaseIndex()){
			throw new RuntimeException("Error: location is out of bounds of this sequence");
		}
		if(loc.getChromosome().equals(this.getEnd().getChromosome()) && loc.getBaseIndex() > this.getEnd().getBaseIndex()){
			throw new RuntimeException("Error: location is out of bounds of this sequence");
		}
	}
	
	private int getIndexOf(GenomicCoordinate loc){
		int chrIndex = m_ChrIndexes.get(loc.getChromosome());
		if(loc.getChromosome().equals(this.getStart().getChromosome())){
			return (int) (loc.getBaseIndex() - this.getStart().getBaseIndex());
		}
		return chrIndex + (int) loc.getBaseIndex() - 1;
	}
	
	/**
	 * Returns the base, represented as a character, at the given GenomicLocation. If the location
	 * is not contained within this sequence or the location is null, a RuntimeException is thrown.
	 * @param loc - location of the base to be returned
	 * @return a character representing the base within this GenomicSequence at the given GenomicLocation
	 */
	public char getBaseAt(GenomicCoordinate loc){
		this.checkLocation(loc);
		return m_Sequence.charAt(this.getIndexOf(loc));
	}
	
	/**
	 * Returns the sequence contained by this GenomicSequence
	 * @return
	 */
	public String getSequence(){
		return m_Sequence;
	}
	
	/**
	 * Returns the GenomicRegion where this sequence is located
	 * @return
	 */
	public GenomicRegion getRegion(){
		return m_Region;
	}
	
	@Override
	public String toString(){
		return m_Sequence + "\t" + m_Region.toString();
	}
	
	@Override
	public boolean equals(Object o){
		if(o == null) return false;
		if(o instanceof GenomicSequence){
			GenomicSequence other = (GenomicSequence) o;
			return m_Region.equals(other.m_Region) && m_Sequence.equals(other.m_Sequence);
		}
		return false;
	}
	
	@Override
	public int hashCode(){
		return m_HashCode;
	}

	@Override
	public int compareTo(GenomicSequence o) {
		if(!m_Region.equals(o.m_Region)){
			return m_Region.compareTo(o.m_Region);
		}
		return m_Sequence.compareTo(o.m_Sequence);
	}

}
