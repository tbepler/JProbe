package plugins.genome.services.utils;

import java.io.Serializable;
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
	
	private int getIndexOf(GenomicCoordinate coord){
		return (int) this.getStart().distance(coord);
	}
	
	public GenomicSequence subsequence(GenomicCoordinate start, GenomicCoordinate end){
		if(this.getGenomicContext() != start.getGenomicContext() || this.getGenomicContext() != end.getGenomicContext()){
			throw new RuntimeException("Error: GenomicSequence cannot be subsequenced using coordinates from different genomes");
		}
		if(!this.contains(start) || !this.contains(end)){
			throw new RuntimeException("Error: cannot subsequence using coordinates not contained by this GenomicSequence");
		}
		
		if(start.compareTo(end) > 0){
			GenomicCoordinate swap = start;
			start = end;
			end = swap;
		}
		int startIndex = this.getIndexOf(start);
		int endIndex = this.getIndexOf(end);
		
		String subSeq = m_Sequence.substring(startIndex, endIndex + 1);
		GenomicRegion subRegion = new GenomicRegion(this.getGenomicContext(), start, end);
		
		return new GenomicSequence(subSeq, subRegion);
	}
	
	public GenomicSequence subsequence(GenomicCoordinate start){
		return this.subsequence(start, this.getEnd());
	}
	
	/**
	 * Splits this GenomicSequence into 2 GenomicSequences around the given GenomicLocation. The second sequence
	 * will start at the location, while the first sequence will end just before the given location.
	 * @param loc - location around which to split this GenomicSequence
	 * @return and array containing two GenomicSequences. The first being before the location and the second starting
	 * at the location
	 */
	public GenomicSequence[] split(GenomicCoordinate coord){
		if(this.getGenomicContext() != coord.getGenomicContext()){
			throw new RuntimeException("Error: cannot split this sequence around a coordinate from a different genome. "+this.getGenomicContext()+" and "+coord.getGenomicContext());
		}
		if(!m_Region.contains(coord)){
			throw new RuntimeException("Error: coordinate "+coord+" does not fall within this sequence.");
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
	
	/**
	 * Returns the base, represented as a character, at the given GenomicLocation. If the location
	 * is not contained within this sequence or the location is null, a RuntimeException is thrown.
	 * @param loc - location of the base to be returned
	 * @return a character representing the base within this GenomicSequence at the given GenomicLocation
	 */
	public char getBaseAt(GenomicCoordinate coord){
		if(this.getGenomicContext() != coord.getGenomicContext()){
			throw new RuntimeException("Error: cannot find base for a coordinate in a different genome. "+this.getGenomicContext()+" and "+coord.getGenomicContext());
		}
		if(!this.contains(coord)){
			throw new RuntimeException("Error: this sequence does not contain "+coord);
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

}
