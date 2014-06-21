package util.genome;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import util.DNAUtils;

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
	
	public int toIndex(GenomicCoordinate c){
		return m_Region.toIndex(c);
	}
	
	public GenomicSequence reverseCompliment(){
		return new GenomicSequence(DNAUtils.reverseCompliment(m_Sequence), m_Region);
	}
	
	/**
	 * Joins the other GenomicSequence to this one, assuming that these are both on the plus strand.
	 * In other words, this returns a new GenomicSequence with is the result of taking the first
	 * GenomicSequence by natural ordering and appending the second to it.
	 * @param other
	 * @return
	 */
	public GenomicSequence join(GenomicSequence other){
		if(!m_Region.adjacentTo(other.getRegion()) && !m_Region.overlaps(other.getRegion())){
			throw new RuntimeException("Cannot join two GenomicSequences that are not overlapping or adjacent");
		}
		GenomicSequence first = m_Region.compareTo(other.getRegion()) < 1 ? this : other;
		GenomicSequence second = first != this ? this : other;
		int overlap = (int) m_Region.getOverlap(other.getRegion());
		if(!first.getSequence().substring(first.length() - overlap).equals(second.getSequence().substring(0, overlap))){
			throw new RuntimeException("Cannot join GenomicSequences: overlap regions do not match");
		}
		String seq = first.getSequence() + second.getSequence().substring(overlap);
		return new GenomicSequence(seq, m_Region.union(other.getRegion()));
	}
	
	/**
	 * Joins the given GenomicSequence to this GenomicSequence in accordance with the given strand.
	 * If the strand is unknown, then it is assumed to be the plus strand. GenomicSequences are joined
	 * by taking the first sequence by natural ordering (if strand is PLUS) or the second sequence
	 * by natural ordering (if strand is MINUS) and then appending the other sequence to its end.
	 * <p>
	 * Strand == PLUS: new sequence is First + Second
	 * <p>
	 * Strand == MINUS: new sequence is Second + First
	 * @param other
	 * @param strand
	 * @return
	 */
	public GenomicSequence join(GenomicSequence other, Strand strand){
		if(strand != Strand.MINUS){
			return this.join(other);
		}
		GenomicSequence first = m_Region.compareTo(other.getRegion()) < 1 ? this : other;
		GenomicSequence second = first != this ? this : other;
		int overlap = (int) m_Region.getOverlap(other.getRegion());
		if(!second.getSequence().substring(second.length() - overlap).equals(first.getSequence().substring(0, overlap))){
			throw new RuntimeException("Cannot join GenomicSequences: overlap regions do not match");
		}
		String seq = second.getSequence() + first.getSequence().substring(overlap);
		return new GenomicSequence(seq, m_Region.union(other.getRegion()));
	}
	
	public boolean adjacentTo(GenomicSequence other){
		return m_Region.adjacentTo(other.getRegion());
	}
	
	public boolean overlaps(GenomicSequence other){
		return m_Region.overlaps(other.getRegion());
	}
	
	public boolean overlaps(GenomicRegion region){
		return m_Region.overlaps(region);
	}
	
	public long getOverlap(GenomicRegion other){
		return m_Region.getOverlap(other);
	}
	
	public long getOverlap(GenomicSequence other){
		return m_Region.getOverlap(other.getRegion());
	}
	
	private int getIndexOf(GenomicCoordinate coord){
		return (int) this.getStart().distance(coord);
	}
	
	/**
	 * Returns the GenomicCoordinate of the base in this sequence at the given string index.
	 * Assumes that this sequence is on the positive strand.
	 * @param index
	 * @return
	 */
	public GenomicCoordinate toCoordinate(int index){
		return m_Region.toCoordinate(index);
	}
	
	/**
	 * Returns the GenomicCoordinate of the base in this sequence at the given string index.
	 * If this sequence is on the minus strand, then an index of 0 will correspond to
	 * the last coordinate of this sequence.
	 * @param index
	 * @param strand
	 * @return
	 */
	public GenomicCoordinate toCoordinate(int index, Strand strand){
		if(strand != Strand.MINUS){
			return this.toCoordinate(index);
		}
		return m_Region.mirror(m_Region.toCoordinate(index));
	}
	
	public GenomicSequence appendPrefix(String prefix){
		GenomicCoordinate newStart = this.getStart().decrement(prefix.length());
		String seq = prefix + m_Sequence;
		return new GenomicSequence(seq, new GenomicRegion(newStart, this.getEnd()));
	}
	
	public GenomicSequence appendSuffix(String suffix){
		GenomicCoordinate newEnd = this.getEnd().increment(suffix.length());
		String seq = m_Sequence + suffix;
		return new GenomicSequence(seq, new GenomicRegion(this.getStart(), newEnd));
	}
	
	/**
	 * This method returns a new GenomicSequence that is a subsequence of this one starting
	 * at the given start coordinate and ending on the given end coordinate. Assumes that this
	 * GenomicSequence occurs on the PLUS strand.
	 * @param start
	 * @param end
	 * @return
	 */
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
	
	public GenomicSequence subsequence(GenomicCoordinate start, GenomicCoordinate end, Strand strand){
		if(strand != Strand.MINUS){
			return this.subsequence(start, end);
		}
		if(!this.contains(start) || !this.contains(end)){
			throw new RuntimeException("Cannot subsequence using coordinates not contained by this GenomicSequence");
		}
		if(start.compareTo(end) > 0){
			GenomicCoordinate swap = start;
			start = end;
			end = swap;
		}
		//mirror the start and end coordinates to correctly reflect locations on the minus strand sequence
		int endIndex = this.getIndexOf(m_Region.mirror(start));
		int startIndex = this.getIndexOf(m_Region.mirror(end));
		
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
	
	public GenomicSequence[] split(GenomicRegion region){
		if(region.getEnd().equals(m_Region.getEnd())){
			return new GenomicSequence[]{
					this.split(region.getStart())[0],
					null
				};
		}
		return new GenomicSequence[]{
			this.split(region.getStart())[0],
			this.split(region.getEnd().increment(1))[1]
		};
	}
	
	public GenomicSequence[] split(GenomicRegion[] r){
		Set<GenomicRegion> regions = new TreeSet<GenomicRegion>();
		for(GenomicRegion reg : r){
			regions.add(reg);
		}
		List<GenomicSequence> split = new ArrayList<GenomicSequence>();
		GenomicSequence remainder = this;
		for(GenomicRegion region : regions){
			GenomicSequence[] partition = remainder.split(region);
			if(partition[0] != null){
				split.add(partition[0]);
			}
			remainder = partition[1];
		}
		if(remainder != null){
			split.add(remainder);
		}
		return split.toArray(new GenomicSequence[split.size()]);
	}
	
	/**
	 * Tests whether the given GenomicLocation occurs within this GenomicSequence
	 * @param loc - GenomicLocation to test
	 * @return True if the given location occurs within this sequence, False otherwise
	 */
	public boolean contains(GenomicCoordinate coord){
		return m_Region.contains(coord);
	}
	
	public boolean contains(GenomicRegion region){
		return m_Region.contains(region);
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
	 * Returns a new GenomicSequence that is a copy of this one with the specified mutation made.
	 * @param coord - coordinate at which to mutate.
	 * @param newBase - the new base that should be at the specified coordinate
	 * @return
	 */
	public GenomicSequence mutate(GenomicCoordinate coord, char newBase){
		int index = this.getIndexOf(coord);
		String newSeq = m_Sequence.substring(0, index) + newBase + m_Sequence.substring(index + 1);
		return new GenomicSequence(newSeq, m_Region);
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
	 * Returns the base, represented as a character, at the given GenomicCoordinate. If the location
	 * is not contained within this sequence or the location is null, a RuntimeException is thrown.
	 * Assumes that this sequence is on the plus strand.
	 * @param coordinate - coordinate of the base to be returned
	 * @return a character representing the base within this GenomicSequence at the given GenomicCoordinate
	 */
	public char getBaseAt(GenomicCoordinate coord){
		if(!this.contains(coord)){
			throw new RuntimeException("This sequence does not contain "+coord);
		}
		return m_Sequence.charAt(this.getIndexOf(coord));
	}
	
	/**
	 * Returns the base, represented as a character, at the given GenomicCoordinate. If the strand
	 * is MINUS, then the coordinate will be mirrored and then treated as if this strand were PLUS.
	 * This is because coordinates are always according to the PLUS strand. Therefore the last coordinate
	 * of this region corresponds to the first character in this sequences string, if the string represents
	 * the MINUS strand.
	 * @param coord - coordinate of the base to be retrieved
	 * @param strand - the strand on which this sequence occurs
	 * @return
	 */
	public char getBaseAt(GenomicCoordinate coord, Strand strand){
		if(strand == Strand.MINUS){
			return getBaseAt(m_Region.mirror(coord));
		}
		return getBaseAt(coord);
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
		if(this == o) return 0;
		if(o == null) return -1;
		int regionComp = m_Region.compareTo(o.m_Region);
		if(regionComp != 0) return regionComp;
		return m_Sequence.compareTo(o.m_Sequence);
	}

	@Override
	public Iterator<GenomicCoordinate> iterator() {
		return m_Region.iterator();
	}

}
