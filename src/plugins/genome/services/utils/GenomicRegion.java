package plugins.genome.services.utils;

import java.io.Serializable;
import java.util.Comparator;

import org.apache.commons.lang3.builder.HashCodeBuilder;

public class GenomicRegion implements Comparable<GenomicRegion>, Serializable {
	private static final long serialVersionUID = 1L;
	
	private static final String CHR_PREFIX = "Chr";
	private static final char CHR_SEP = ':';
	private static final char LOC_SEP = '-';
	
	private static final String SAME_CHR_REGEX = "^"+CHR_PREFIX+".+"+CHR_SEP+"\\d+"+LOC_SEP+"\\d+$";
	
	static GenomicRegion parseString(GenomicContext context, String s) throws ParsingException{
		s = s.trim();
		try{
			if(s.matches(SAME_CHR_REGEX)){
				Chromosome chr = context.getChr(s.substring(CHR_PREFIX.length(),s.indexOf(CHR_SEP)));
				long start = Long.parseLong(s.substring(s.indexOf(CHR_SEP)+1, s.indexOf(LOC_SEP)));
				long end = Long.parseLong(s.substring(s.indexOf(LOC_SEP)+1));
				return new GenomicRegion(context, new GenomicCoordinate(context, chr, start), new GenomicCoordinate(context, chr, end));
			}else{
				String[] parts = s.split(String.valueOf(LOC_SEP));
				return new GenomicRegion(context, GenomicCoordinate.parseString(context, parts[0].trim()), GenomicCoordinate.parseString(context, parts[1].trim()));
			}
		} catch (Exception e){
			throw new ParsingException(e);
		}
	}
	
	private final GenomicCoordinate m_Start;
	private final GenomicCoordinate m_End;
	private final GenomicContext m_Context;
	private final long m_Size;
	private final int m_Hash;
	
	GenomicRegion(GenomicContext context, GenomicCoordinate start, GenomicCoordinate end){
		if(context != start.getGenomicContext()){
			throw new RuntimeException("Error: genomic coordinate "+start+" does not occur within the genome "+context);
		}
		if(context != end.getGenomicContext()){
			throw new RuntimeException("Error: genomic coordinate "+end+" does not occur within the genome "+context);
		}
		m_Context = context;
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
	
	private int computeHash(){
		return new HashCodeBuilder(367, 821).append(m_Start).append(m_End).append(m_Context).toHashCode();
	}
	
	private long computeSize(){
		Chromosome cur = m_Start.getChromosome();
		if(cur.equals(m_End.getChromosome())){
			return m_End.getBaseIndex() - m_Start.getBaseIndex() + 1;
		}
		long size = 0;
		size += cur.getSize() - m_Start.getBaseIndex() + 1;
		cur = cur.nextChr();
		while(!cur.equals(m_End.getChromosome())){
			size += cur.getSize();
		}
		size += m_End.getBaseIndex();
		return size;
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
	
	public boolean contains(GenomicCoordinate coordinate){
		return m_Start.compareTo(coordinate) <= 0 && m_End.compareTo(coordinate) >= 0;
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
			return new GenomicRegion(m_Context, other.m_Start, m_End);
		}
		return new GenomicRegion(m_Context, m_Start, other.m_End);
	}
	
	public GenomicRegion union(GenomicRegion other, Comparator<GenomicCoordinate> comparator){
		GenomicCoordinate newStart = comparator.compare(m_Start, other.m_Start) > 0 ? other.m_Start : m_Start;
		GenomicCoordinate newEnd = comparator.compare(m_End, other.m_End) < 0 ? other.m_End : m_End;
		return new GenomicRegion(newStart, newEnd);
	}
	
	public GenomicRegion union(GenomicRegion other){
		GenomicCoordinate newStart = m_Start.compareTo(other.m_Start) > 0 ? other.m_Start : m_Start;
		GenomicCoordinate newEnd = m_End.compareTo(other.m_End) < 0 ? other.m_End : m_End;
		return new GenomicRegion(newStart, newEnd);
	}
	
	@Override
	public String toString(){
		if(m_Start.getChromosome().equals(m_End.getChromosome())){
			//chromosomes are the same, so write in condensed chromosome form
			return CHR_PREFIX+m_Start.getChromosome()+CHR_SEP+m_Start.getBaseIndex()+LOC_SEP+m_End.getBaseIndex();
		}else{
			//chromosomes are different, so write both chromosomes
			return m_Start.toString() + LOC_SEP + m_End.toString();
		}
	}
	
	@Override
	public boolean equals(Object o){
		if(o == null) return false;
		if(this == o) return true;
		if(o instanceof GenomicRegion){
			GenomicRegion other = (GenomicRegion) o;
			return m_Start.equals(other.m_Start) && m_End.equals(other.m_End) && m_Context == other.m_Context;
		}
		return false;
	}
	
	@Override
	public int hashCode(){
		return m_Hash;
	}
	
	@Override
	public int compareTo(GenomicRegion o) {
		Comparator<GenomicRegion> comparator = m_Context.getStartAscendingComparator();
		return comparator.compare(this, o);
	}

	
	
	
	
	
}