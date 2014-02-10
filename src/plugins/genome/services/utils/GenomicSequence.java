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
	private final Map<Chromosome, Integer> m_ChrIndexes;
	private final int m_HashCode;
	
	public GenomicSequence(String sequence, GenomicRegion region, Map<Chromosome, Integer> chrIndexes){
		m_Sequence = sequence;
		m_Region = region;
		m_ChrIndexes = new HashMap<Chromosome, Integer>(chrIndexes);
		m_HashCode = this.computeHash();
	}
	
	public GenomicSequence(String sequence, GenomicRegion region){
		this(sequence, region, generateChrMap(sequence, region));
	}
	
	private static Map<Chromosome, Integer> generateChrMap(String sequence, GenomicRegion region){
		Map<Chromosome, Integer> chrMap = new HashMap<Chromosome, Integer>();
		Chromosome start = region.getStart().getChromosome();
		chrMap.put(start, 0);
		Chromosome end = region.getEnd().getChromosome();
		chrMap.put(end, sequence.length() - (int) region.getEnd().getBaseIndex());
		return chrMap;
	}
	
	private int computeHash(){
		return new HashCodeBuilder(439, 61).append(m_Region).append(m_Sequence).toHashCode();
	}
	
	private static Map<Chromosome, Integer> joinChrMaps(final Map<Chromosome, Integer> first, final Map<Chromosome, Integer> second, final int firstSeqLen){
		Map<Chromosome, Integer> joined = new HashMap<Chromosome, Integer>();
		for(Chromosome chr : first.keySet()){
			joined.put(chr, first.get(chr));
		}
		PriorityQueue<Chromosome> chrQ = new PriorityQueue<Chromosome>(10, new Comparator<Chromosome>(){
			@Override
			public int compare(Chromosome o1, Chromosome o2) {
				return second.get(o1) - second.get(o2);
			}
		});
		for(Chromosome chr : second.keySet()){
			chrQ.add(chr);
		}
		Chromosome prev = null;
		int prevIndex = 0;
		while(!chrQ.isEmpty()){
			Chromosome chr = chrQ.poll();
			if(joined.containsKey(chr)){
				prev = chr;
				prevIndex = joined.get(prev);
				continue;
			}else{
				if(prev == null){
					joined.put(chr, firstSeqLen);
					prev = chr;
					prevIndex = firstSeqLen;
				}else{
					int delta = second.get(chr) - second.get(prev);
					joined.put(chr, prevIndex + delta);
					prev = chr;
					prevIndex = prevIndex + delta;
				}
			}
		}
		return joined;
	}
	
	private int overlapLength(GenomicSequence next){
		Chromosome firstChr = next.getStart().getChromosome();
		if(m_ChrIndexes.containsKey(firstChr)){
			return (int) (m_Sequence.length() - m_ChrIndexes.get(firstChr) - next.getStart().getBaseIndex() + 1);
		}
		return 0;
	}
	
	/**
	 * Appends the given GenomicSequence to the END of this sequence. This does not check to make sure there is
	 * no extra space between the sequences or whether the given sequence should actually follow this sequence.
	 * Therefore, if those conditions are not met, results may be unpredictable.
	 * @param next - the GenomicSequence to append to the end of this sequence
	 * @param comparator - Comparator for use in unioning the GenomicRegions 
	 * @return a GenomicSequence that is this sequence with the next sequence appended to the end
	 */
	public GenomicSequence append(GenomicSequence next, Comparator<GenomicLocation> comparator){
		Map<Chromosome, Integer> joined = joinChrMaps(m_ChrIndexes, next.m_ChrIndexes, m_Sequence.length());
		GenomicRegion union = m_Region.union(next.m_Region, comparator);
		String combinedSequence = m_Sequence + next.m_Sequence.substring(this.overlapLength(next));
		return new GenomicSequence(combinedSequence, union, joined);
	}
	
	/**
	 * Splits this GenomicSequence into 2 GenomicSequences around the given GenomicLocation. The second sequence
	 * will start at the location, while the first sequence will end just before the given location.
	 * @param loc - location around which to split this GenomicSequence
	 * @return and array containing two GenomicSequences. The first being before the location and the second starting
	 * at the location
	 */
	public GenomicSequence[] split(GenomicLocation loc){
		this.checkLocation(loc);
		int index = getIndexOf(loc);
	}
	
	/**
	 * Tests whether the given GenomicLocation occurs within this GenomicSequence
	 * @param loc - GenomicLocation to test
	 * @param comparator - comparator to use for comparing GenomicLocations
	 * @return True if the given location occurs within this sequence, False otherwise
	 */
	public boolean contains(GenomicLocation loc, Comparator<GenomicLocation> comparator){
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
	public GenomicLocation getStart(){
		return m_Region.getStart();
	}
	
	/**
	 * Returns the ending location of this GenomicSequence
	 * @return a GenomicLocation representing the last position of this sequence
	 */
	public GenomicLocation getEnd(){
		return m_Region.getEnd();
	}
	
	private void checkLocation(GenomicLocation loc){
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
	
	private int getIndexOf(GenomicLocation loc){
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
	public char getBaseAt(GenomicLocation loc){
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
