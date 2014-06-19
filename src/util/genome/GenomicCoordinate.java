package util.genome;

import java.io.Serializable;
import java.util.Comparator;

import org.apache.commons.lang3.builder.HashCodeBuilder;

public class GenomicCoordinate implements Comparable<GenomicCoordinate>, Serializable{
	private static final long serialVersionUID = 1L;
	
	public static final Comparator<GenomicCoordinate> ASCENDING_COMPARATOR = new Comparator<GenomicCoordinate>(){

		@Override
		public int compare(GenomicCoordinate o1, GenomicCoordinate o2) {
			return o1.naturalCompareTo(o2);
		}
		
	};
	
	public static final Comparator<GenomicCoordinate> DESCENDING_COMPARATOR = new Comparator<GenomicCoordinate>(){

		@Override
		public int compare(GenomicCoordinate o1, GenomicCoordinate o2) {
			return -o1.naturalCompareTo(o2);
		}
		
	};
	
	private static final char SEP = ':';
	
	public static final String COORD_REGEX = ".+"+SEP+"\\d+";
	
	public static GenomicCoordinate parseString(String s) throws ParsingException{
		try{
			String chr = s.substring(0, s.indexOf(SEP));
			Chromosome c = Chromosome.getInstance(chr);
			long baseIndex = Long.parseLong(s.substring(s.indexOf(SEP)+1));
			return new GenomicCoordinate(c, baseIndex);
		} catch (Exception e){
			throw new ParsingException(e);
		}
	}
	
	private final Chromosome m_Chr;
	private final long m_BaseIndex;
	private final int m_Hash;
	
	public GenomicCoordinate(Chromosome chr, int baseIndex){
		this(chr, (long)baseIndex);
	}
	
	public GenomicCoordinate(Chromosome chr, long baseIndex){
		m_Chr = chr;
		m_BaseIndex = baseIndex;
		m_Hash = this.computeHash();
	}
	
	public GenomicCoordinate(String chrom, long baseIndex){
		this(Chromosome.getInstance(chrom), baseIndex);
	}
	
	private int computeHash(){
		return new HashCodeBuilder(11, 461).append(m_Chr).append(m_BaseIndex).toHashCode();
	}
	
	public long distance(GenomicCoordinate other){
		if(!m_Chr.equals(other.m_Chr)){
			throw new RuntimeException("Cannot find the distance between coordinates on different chromosomes");
		}
		return Math.abs(this.getBaseIndex() - other.getBaseIndex());
	}
	
	public GenomicCoordinate increment(int numBases){
		if(numBases == 0) return this;
		long newIndex = m_BaseIndex + numBases;
		if(newIndex < 1){
			throw new RuntimeException("Cannot decrement to a base index less than one");
		}
		return new GenomicCoordinate(m_Chr, newIndex);
	}
	
	public GenomicCoordinate decrement(int numBases){
		return this.increment(-numBases);
	}
	
	public Chromosome getChromosome(){
		return m_Chr;
	}
	
	public long getBaseIndex(){
		return m_BaseIndex;
	}
	
	@Override
	public String toString(){
		return m_Chr.toString()+SEP+m_BaseIndex;
	}
	
	@Override
	public boolean equals(Object o){
		if(o == null) return false;
		if(this == o) return true;
		if(o instanceof GenomicCoordinate){
			GenomicCoordinate other = (GenomicCoordinate) o;
			return m_Chr.equals(other.m_Chr) && m_BaseIndex == other.m_BaseIndex;
		}
		return false;
	}
	
	@Override
	public int hashCode(){
		return m_Hash;
	}
	
	protected int naturalCompareTo(GenomicCoordinate o){
		int chrComp = m_Chr.compareTo(o.m_Chr);
		if(chrComp != 0) return chrComp;
		if(m_BaseIndex > o.m_BaseIndex){
			return 1;
		}
		if(m_BaseIndex < o.m_BaseIndex){
			return -1;
		}
		return 0;
	}

	@Override
	public int compareTo(GenomicCoordinate o) {
		if(o == this) return 0;
		if(o == null) return -1;
		return this.naturalCompareTo(o);
	}
	
	
	
	
}
