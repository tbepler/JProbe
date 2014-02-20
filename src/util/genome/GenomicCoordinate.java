package util.genome;

import java.io.Serializable;
import java.util.Comparator;

import org.apache.commons.lang3.builder.HashCodeBuilder;

public class GenomicCoordinate implements Comparable<GenomicCoordinate>, Serializable{
	private static final long serialVersionUID = 1L;
	
	private static final char SEP = ':';
	
	public static GenomicCoordinate parseString(String s) throws ParsingException{
		return parseString(null, s);
	}
	
	public static GenomicCoordinate parseString(GenomicContext context, String s) throws ParsingException{
		try{
			String chr = s.substring(0, s.indexOf(SEP));
			Chromosome c = context.getChr(chr);
			long baseIndex = Long.parseLong(s.substring(s.indexOf(SEP)+1));
			return new GenomicCoordinate(context, c, baseIndex);
		} catch (Exception e){
			throw new ParsingException(e);
		}
	}
	
	private final Chromosome m_Chr;
	private final long m_BaseIndex;
	private final GenomicContext m_Context;
	private final int m_Hash;
	
	public GenomicCoordinate(Chromosome chr, int baseIndex){
		this(null, chr, baseIndex);
	}
	
	public GenomicCoordinate(Chromosome chr, long baseIndex){
		this(chr.getGenomicContext(), chr, baseIndex);
	}
	
	public GenomicCoordinate(GenomicContext context, Chromosome chr, long baseIndex){
		if(context != null && !context.hasChr(chr)){
			throw new RuntimeException("Error: chromosome "+chr+" not found in genome "+context);
		}
		if(chr.getSize() >= 0 && baseIndex > chr.getSize()){
			throw new RuntimeException("Error: index "+baseIndex+" is out of bounds on chromosome "+chr+"\n"+chr+" is only "+chr.getSize()+" bases long");
		}
		m_Chr = chr;
		m_BaseIndex = baseIndex;
		m_Context = context;
		m_Hash = this.computeHash();
	}
	
	public GenomicCoordinate(GenomicContext context, Chromosome chr, int baseIndex){
		this(context, chr, (long) baseIndex); 
	}
	
	private int computeHash(){
		return new HashCodeBuilder(11, 461).append(m_Chr).append(m_BaseIndex).append(m_Context).toHashCode();
	}
	
	public GenomicContext getGenomicContext(){
		return m_Context;
	}
	
	public boolean hasReferenceGenome(){
		return m_Context != null;
	}
	
	public long distance(GenomicCoordinate other){
		if(m_Context == null && !m_Chr.equals(other.m_Chr)){
			throw new RuntimeException("Error: cannot find the distance between coordinates on different chromosomes without a reference genome");
		}
		if(m_Context != other.getGenomicContext()){
			throw new RuntimeException("Error: cannot find the distance between coordinates from different genomes. "+m_Context+" and "+other.getGenomicContext());
		}
		GenomicCoordinate start = this.compareTo(other) < 1 ? this : other;
		GenomicCoordinate end = start != this ? this : other;
		Chromosome cur = start.getChromosome();
		if(cur.equals(end.getChromosome())){
			return end.getBaseIndex() - start.getBaseIndex();
		}
		long size = 0;
		size += cur.getSize() - start.getBaseIndex() + 1;
		cur = cur.nextChr();
		while(!cur.equals(end.getChromosome())){
			size += cur.getSize();
			cur = cur.nextChr();
		}
		size += end.getBaseIndex() - 1;
		return size;
	}
	
	public GenomicCoordinate increment(int numBases){
		if(numBases == 0) return this;
		long newIndex = m_BaseIndex + numBases;
		if(newIndex < 1 && m_Context == null){
			throw new RuntimeException("Error: cannot decrement to a base index less than one without a reference genome");
		}
		if(m_Chr.getSize() < 0 && newIndex >= 1){
			return new GenomicCoordinate(m_Context, m_Chr, newIndex);
		}
		if(m_Chr.getSize() >= 0 && newIndex > m_Chr.getSize() && m_Context == null){
			throw new RuntimeException("Error: cannot increment to a base index larger than chromosome size without a reference genome");
		}
		if(newIndex > m_Chr.getSize()){
			Chromosome chr = m_Chr;
			while(newIndex > chr.getSize()){
				newIndex -= chr.getSize();
				chr = chr.nextChr();
				if(chr == null) return null;
			}
			return new GenomicCoordinate(m_Context, chr, newIndex);
		}
		if(newIndex < 1){
			Chromosome chr = m_Chr;
			while(newIndex < 1){
				chr = m_Chr.prevChr();
				if(chr == null) return null;
				newIndex += chr.getSize();
			}
			return new GenomicCoordinate(m_Context, chr, newIndex);
		}
		return new GenomicCoordinate(m_Context, m_Chr, newIndex);
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
			return m_Chr.equals(other.m_Chr) && m_BaseIndex == other.m_BaseIndex && m_Context == other.m_Context;
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
		if(o == null) return -1;
		if(m_Context == null){
			return this.naturalCompareTo(o);
		}
		Comparator<GenomicCoordinate> comparator = m_Context.getLocationAscendingComparator();
		return comparator.compare(this, o);
	}
	
	
	
	
}
