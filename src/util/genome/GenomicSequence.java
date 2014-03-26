package util.genome;

import java.io.Serializable;
import java.util.Iterator;

import org.apache.commons.lang3.builder.HashCodeBuilder;

public class GenomicSequence implements Serializable, Comparable<GenomicSequence>, Iterable<GenomicCoordinate>{
	private static final long serialVersionUID = 1L;
	
	private final String m_Sequence;
	private final GenomicRegion m_Region;
	private final int m_HashCode;
	
	public GenomicSequence(String sequence, GenomicRegion region){
		if(sequence.length() != region.getSize()){
			throw new RuntimeException("Sequence and region are of different sizes");
		}
		m_Sequence = sequence;
		m_Region = region;
		m_HashCode = this.computeHash();
	}
	
	private int computeHash(){
		return new HashCodeBuilder(439, 61).append(m_Region).append(m_Sequence).toHashCode();
	}
	
	public GenomicSequence join(GenomicSequence other){
		if(!m_Region.adjacentTo(other.getRegion()) && !m_Region.overlaps(other.getRegion())){
			throw new RuntimeException("Cannot join two GenomicSequences that are not overlapping or adjacent");
		}
		GenomicSequence first = m_Region.compareTo(other.getRegion()) < 1 ? this : other;
		GenomicSequence second = first != this ? this : other;
		int overlap = (int) m_Region.getOverlap(other.getRegion());
		String seq = first.getSequence() + second.getSequence().substring(overlap);
		return new GenomicSequence(seq, m_Region.union(other.getRegion()));
	}
	
	public boolean adjacentTo(GenomicSequence other){
		return m_Region.adjacentTo(other.getRegion());
	}
	
	public boolean overlaps(GenomicSequence other){
		return m_Region.overlaps(other.getRegion());
	}
	
	private int getIndexOf(GenomicCoordinate coord){
		return (int) this.getStart().distance(coord);
	}
	
	public GenomicSequence subsequence(GenomicCoordinate start, GenomicCoordinate end){
		if(!this.contains(start) || !this.contains(end)){
			throw new RuntimeException("Cannot subsequence using coordinates not contained by this GenomicSequence");
		}
		
		if(start.compareTo(end) > 0){
			GenomicCoordinate swap = start;
			start = end;
			end = swap;
		}
		int startIndex = this.getIndexOf(start);
		int endIndex = this.getIndexOf(end);
		
		String subSeq = m_Sequence.substring(startIndex, endIndex + 1);
		GenomicRegion subRegion = new GenomicRegion(start, end);
		
		return new GenomicSequence(subSeq, subRegion);
	}
	
	public GenomicSequence subsequence(GenomicCoordinate start){
		return this.subsequence(start, this.getEnd());
	}
	
	public GenomicSequence subsequence(GenomicRegion region){
		return this.subsequence(region.getStart(), region.getEnd());
	}
	
	
	/**
	 * Splits this GenomicSequence into 2 GenomicSequences around the given GenomicLocation. The second sequence
	 * will start at the location, while the first sequence will end just before the given location.
	 * @param loc - location around which to split this GenomicSequence
	 * @return and array containing two GenomicSequences. The first being before the location and the second starting
	 * at the location
	 */
	public GenomicSequence[] split(GenomicCoordinate coord){
		if(!m_Region.contains(coord)){
			throw new RuntimeException("Coordinate "+coord+" does not fall within this sequence.");
		}
		if(coord.equals(this.getStart())){
			return new GenomicSequence[]{this, this};
		}
		int index = this.getIndexOf(coord);
		GenomicRegion[] splitRegion = m_Region.split(coord);
		return new GenomicSequence[]{
				new GenomicSequence(m_Sequence.substring(0, index), splitRegion[0]),
				new GenomicSequence(m_Sequence.substring(index), splitRegion[1])
				};
	}
	
	/**
	 * Tests whether the given GenomicLocation occurs within this GenomicSequence
	 * @param loc - GenomicLocation to test
	 * @return True if the given location occurs within this sequence, False otherwise
	 */
	public boolean contains(GenomicCoordinate coord){
		return m_Region.contains(coord);
	}
	
	/**
	 * Tests whether the given GenomicSequence occurs within this GenomicSequence. This requires
	 * that the given sequence's region is contained by this sequence and that the strings match
	 * exactly at that location.
	 * @param sequence - GenomicSequence to test
	 * @return True if the given GenomicSequence is contained within this sequence as defined above.
	 * False otherwise.
	 */
	public boolean contains(GenomicSequence sequence){
		if(!this.contains(sequence.getStart()) || !this.contains(this.getEnd())){
			return false;
		}
		GenomicCoordinate cur = sequence.getStart();
		while(cur.compareTo(sequence.getEnd()) <= 0){
			if(this.getBaseAt(cur) != sequence.getBaseAt(cur)){
				return false;
			}
			cur = cur.increment(1);
		}
		return true;
	}
	
	/**
	 * Returns the length of this GenomicSequence
	 * @return
	 */
	public int length(){
		return m_Sequence.length();
	}
	
	/**
	 * Returns the chromosome on which this sequence occurs.
	 * @return
	 */
	public Chromosome getChromosome(){
		return m_Region.getChromosome();
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
	
	/**
	 * Returns the base, represented as a character, at the given GenomicLocation. If the location
	 * is not contained within this sequence or the location is null, a RuntimeException is thrown.
	 * @param loc - location of the base to be returned
	 * @return a character representing the base within this GenomicSequence at the given GenomicLocation
	 */
	public char getBaseAt(GenomicCoordinate coord){
		if(!this.contains(coord)){
			throw new RuntimeException("This sequence does not contain "+coord);
		}
		return m_Sequence.charAt(this.getIndexOf(coord));
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

	@Override
	public Iterator<GenomicCoordinate> iterator() {
		return m_Region.iterator();
	}

}
