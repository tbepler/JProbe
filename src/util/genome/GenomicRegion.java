package util.genome;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Iterator;

import org.apache.commons.lang3.builder.HashCodeBuilder;

public class GenomicRegion implements Comparable<GenomicRegion>, Serializable, Iterable<GenomicCoordinate> {
	private static final long serialVersionUID = 1L;
	
	public static GenomicRegion union(GenomicRegion[] regions){
		if(regions.length > 0){
			GenomicRegion r = regions[0];
			for(int i=1; i<regions.length; i++){
				r = r.union(regions[i]);
			}
			return r;
		}
		return null;
	}
	
	public static final Comparator<GenomicRegion> START_ASCENDING_COMPARATOR = new Comparator<GenomicRegion>(){

		@Override
		public int compare(GenomicRegion o1, GenomicRegion o2) {
			return o1.compareByStart(o2);
		}
		
	};
	
	public static final Comparator<GenomicRegion> START_DESCENDING_COMPARATOR = new Comparator<GenomicRegion>(){

		@Override
		public int compare(GenomicRegion o1, GenomicRegion o2) {
			return -o1.compareByStart(o2);
		}
		
	};
	
	public static final Comparator<GenomicRegion> END_ASCENDING_COMPARATOR = new Comparator<GenomicRegion>(){

		@Override
		public int compare(GenomicRegion o1, GenomicRegion o2) {
			return o1.compareByEnd(o2);
		}
	
	};
	
	public static final Comparator<GenomicRegion> END_DESCENDING_COMPARATOR = new Comparator<GenomicRegion>(){

		@Override
		public int compare(GenomicRegion o1, GenomicRegion o2) {
			return -o1.compareByEnd(o2);
		}
		
	};

	private static final char CHR_SEP = ':';
	private static final char LOC_SEP = '-';
	
	public static final String GENOMIC_REGION_REGEX = ".+"+CHR_SEP+"\\d+"+LOC_SEP+"\\d+";

	public static GenomicRegion parseString(String s) throws ParsingException{
		s = s.trim();
		try{
			Chromosome chr = Chromosome.getInstance(s.substring(0,s.indexOf(CHR_SEP)));
			long start = Long.parseLong(s.substring(s.indexOf(CHR_SEP)+1, s.indexOf(LOC_SEP)));
			long end = Long.parseLong(s.substring(s.indexOf(LOC_SEP)+1));
			return new GenomicRegion(new GenomicCoordinate(chr, start), new GenomicCoordinate(chr, end));
		} catch (Exception e){
			throw new ParsingException(e);
		}
	}
	
	private final GenomicCoordinate m_Start;
	private final GenomicCoordinate m_End;
	private final long m_Size;
	private final int m_Hash;
	
	public GenomicRegion(GenomicCoordinate start, GenomicCoordinate end){
		if(!start.getChromosome().equals(end.getChromosome())){
			throw new RuntimeException("Region start and end must be on the same chromosome");
		}
		if(start.compareTo(end) > 0){
			//start should be before end, so flip them
			m_Start = end;
			m_End = start;
		}else{
			//normal
			m_Start = start;
			m_End = end;
		}
		m_Size = this.computeSize();
		m_Hash = this.computeHash();
	}
	
	public GenomicRegion(Chromosome chrom, long start, long end){
		this(new GenomicCoordinate(chrom, start), new GenomicCoordinate(chrom, end));
	}
	
	private int computeHash(){
		return new HashCodeBuilder(367, 821).append(m_Start).append(m_End).toHashCode();
	}
	
	private long computeSize(){
		return m_Start.distance(m_End) + 1;
	}
	
	public Chromosome getChromosome(){
		return m_Start.getChromosome();
	}
	
	public GenomicCoordinate getStart(){
		return m_Start;
	}
	
	public GenomicCoordinate getEnd(){
		return m_End;
	}
	
	public long getSize(){
		return m_Size;
	}
	
	public GenomicRegion increment(int numBases){
		return new GenomicRegion(m_Start.increment(numBases), m_End.increment(numBases));
	}
	
	public GenomicRegion decrement(int numBases){
		return new GenomicRegion(m_Start.decrement(numBases), m_End.decrement(numBases));
	}
	
	public int toIndex(GenomicCoordinate c){
		if(!this.contains(c)){
			throw new RuntimeException("Region "+this+" does not contain coordinate "+c+".");
		}
		return (int) m_Start.distance(c);
	}
	
	public GenomicCoordinate toCoordinate(int index){
		if(index >= this.getSize()){
			throw new RuntimeException("Index "+index+" out of bounds on region "+this);
		}
		return m_Start.increment(index);
	}
	
	public long distance(GenomicRegion other){
		if(this.overlaps(other)){
			return -this.getOverlap(other);
		}
		if(m_Start.compareTo(other.getStart()) < 0){
			return m_End.distance(other.getStart());
		}
		return other.getEnd().distance(m_Start);
	}
	
	public boolean contains(GenomicCoordinate coordinate){
		return m_Start.compareTo(coordinate) <= 0 && m_End.compareTo(coordinate) >= 0;
	}
	
	public GenomicRegion mirror(GenomicRegion within){
		if(!within.contains(this)){
			throw new RuntimeException("Cannot mirror the GenomicRegion "+this+" within the GenomicRegion "+within+". "+within+
					" does not contain "+this+".");
		}
		int startDist = (int) within.getStart().distance(m_Start);
		int endDist = (int) within.getEnd().distance(m_End);
		GenomicCoordinate newStart = within.getStart().increment(endDist);
		GenomicCoordinate newEnd = within.getEnd().decrement(startDist);
		return new GenomicRegion(newStart, newEnd);
		
	}
	
	/**
	 * Returns a new GenomicCoordinate that is the result of reflecting the given coordinate around the center of this region.
	 * @return
	 */
	public GenomicCoordinate mirror(GenomicCoordinate coord){
		int direction;
		if(coord.compareTo(m_End) < 0){
			direction = 1;
		}else{
			direction = -1;
		}
		int dist = (int) m_End.distance(coord);
		return m_Start.increment(direction * dist);
	}
	
	public boolean contains(GenomicRegion other){
		return this.contains(other.m_Start) && this.contains(other.m_End);
	}
	
	public boolean overlaps(GenomicRegion other){
		return other.contains(this) || this.contains(other.m_Start) || this.contains(other.m_End);
	}
	
	public long getOverlap(GenomicRegion other){
		GenomicRegion overlap = this.intersection(other);
		if(overlap == null) return 0;
		return overlap.getSize();
	}
	
	public GenomicRegion intersection(GenomicRegion other){
		if(!this.overlaps(other)){
			return null;
		}
		if(this.contains(other)){
			return other;
		}
		if(other.contains(this)){
			return this;
		}
		if(this.contains(other.m_Start)){
			return new GenomicRegion(other.m_Start, m_End);
		}
		return new GenomicRegion(m_Start, other.m_End);
	}
	
	public boolean adjacentTo(GenomicRegion other){
		return !this.overlaps(other) && (m_End.increment(1).equals(other.m_Start) || m_Start.decrement(1).equals(other.m_End));
	}
	
	/**
	 * This method creates a GenomicRegion that is the union of this region and the given region. The union
	 * is represented as the region defined by smallest starting position to the largest ending position
	 * of the two regions. In other words, if the regions do not overlap, then the inter-region space will
	 * be included in the unioned region.
	 * @param other - the region to union with this region
	 * @return a new GenomicRegion that starts at the smalles start coordinate and ends at the largest end 
	 * coordinate of the two regions
	 */
	public GenomicRegion union(GenomicRegion other){
		GenomicCoordinate newStart = m_Start.compareTo(other.m_Start) > 0 ? other.m_Start : m_Start;
		GenomicCoordinate newEnd = m_End.compareTo(other.m_End) < 0 ? other.m_End : m_End;
		return new GenomicRegion(newStart, newEnd);
	}
	
	/**
	 * Splits this genomic region into two regions around the given coordinate. The left region
	 * is [start, coordinate) and the right region is [coordinate, end]. This will error
	 * if the coordinate is not within this region or the coordinate is defined using a different
	 * genome from this region.
	 * <p>
	 * Note that because the coordinate is included in the right side region, if the coordinate is
	 * the start of this region, then the right side region will be the full region and the left
	 * side region will contain no bases. Because a region cannot contain zero bases, the full region
	 * will be returned as BOTH the left and right side regions of the array.
	 * @param coordinate - around which this region should be split
	 * @return an array of {left region, right region}
	 */
	public GenomicRegion[] split(GenomicCoordinate coordinate){
		if(!this.contains(coordinate)){
			throw new RuntimeException("The region "+this+" cannot be split around the coordinate "+coordinate);
		}
		if(m_Start.equals(coordinate)){
			return new GenomicRegion[]{this, this};
		}
		GenomicRegion left = new GenomicRegion(m_Start, coordinate.decrement(1));
		GenomicRegion right = new GenomicRegion(coordinate, m_End);
		return new GenomicRegion[]{left, right};
	}
	
	@Override
	public String toString(){
		return m_Start.getChromosome().toString()+CHR_SEP+m_Start.getBaseIndex()+LOC_SEP+m_End.getBaseIndex();
	}
	
	@Override
	public boolean equals(Object o){
		if(o == null) return false;
		if(this == o) return true;
		if(o instanceof GenomicRegion){
			GenomicRegion other = (GenomicRegion) o;
			return m_Start.equals(other.m_Start) && m_End.equals(other.m_End);
		}
		return false;
	}
	
	@Override
	public int hashCode(){
		return m_Hash;
	}
	
	protected int compareByEnd(GenomicRegion o){
		if(this == o) return 0;
		if(o == null) return -1;
		int endComp = m_End.compareTo(o.m_End);
		if(endComp != 0) return endComp;
		return m_Start.compareTo(o.m_End);
	}
	
	protected int compareByStart(GenomicRegion o){
		if(this == o) return 0;
		if(o == null) return -1;
		int startComp = m_Start.compareTo(o.m_Start);
		if(startComp != 0) return startComp;
		return m_End.compareTo(o.m_End);
	}
	
	@Override
	public int compareTo(GenomicRegion o) {
		return this.compareByStart(o);
	}

	@Override
	public Iterator<GenomicCoordinate> iterator() {
		return new Iterator<GenomicCoordinate>(){
			
			private GenomicCoordinate cur = m_Start;
			
			@Override
			public boolean hasNext() {
				return cur.compareTo(m_End) <= 0;
			}

			@Override
			public GenomicCoordinate next() {
				GenomicCoordinate ret = cur;
				cur = cur.increment(1);
				return ret;
			}

			@Override
			public void remove() {
				//do nothing
			}
			
		};
	}

	
	
	
	
	
}