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
	
	public static GenomicRegion parseString(String s){
		s = s.trim();
		try{
			if(s.matches(SAME_CHR_REGEX)){
				Chromosome chr = new Chromosome(s.substring(CHR_PREFIX.length(),s.indexOf(CHR_SEP)));
				long start = Long.parseLong(s.substring(s.indexOf(CHR_SEP)+1, s.indexOf(LOC_SEP)));
				long end = Long.parseLong(s.substring(s.indexOf(LOC_SEP)+1));
				return new GenomicRegion(new GenomicLocation(chr, start), new GenomicLocation(chr, end));
			}else{
				String[] parts = s.split(String.valueOf(LOC_SEP));
				return new GenomicRegion(GenomicLocation.parseString(parts[0].trim()), GenomicLocation.parseString(parts[1].trim()));
			}
		} catch (Exception e){
			return null;
		}
	}
	
	private final GenomicLocation m_Start;
	private final GenomicLocation m_End;
	private final int m_Hash;
	
	public GenomicRegion(GenomicLocation start, GenomicLocation end){
		if(start.compareTo(end) > 0){
			//start should be before end, so flip them
			m_Start = end;
			m_End = start;
		}else{
			//normal
			m_Start = start;
			m_End = end;
		}
		m_Hash = this.computeHash();
	}
	
	private int computeHash(){
		return new HashCodeBuilder(367, 821).append(m_Start).append(m_End).toHashCode();
	}
	
	public GenomicLocation getStart(){
		return m_Start;
	}
	
	public GenomicLocation getEnd(){
		return m_End;
	}
	
	public GenomicRegion union(GenomicRegion other, Comparator<GenomicLocation> comparator){
		GenomicLocation newStart = comparator.compare(m_Start, other.m_Start) > 0 ? other.m_Start : m_Start;
		GenomicLocation newEnd = comparator.compare(m_End, other.m_End) < 0 ? other.m_End : m_End;
		return new GenomicRegion(newStart, newEnd);
	}
	
	public GenomicRegion union(GenomicRegion other){
		GenomicLocation newStart = m_Start.compareTo(other.m_Start) > 0 ? other.m_Start : m_Start;
		GenomicLocation newEnd = m_End.compareTo(other.m_End) < 0 ? other.m_End : m_End;
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
			return m_Start.equals(other.m_Start) && m_End.equals(other.m_End);
		}
		return false;
	}
	
	@Override
	public int hashCode(){
		return m_Hash;
	}
	
	@Override
	public int compareTo(GenomicRegion o) {
		if(m_Start.equals(o.m_Start)){
			return m_End.compareTo(o.m_End);
		}
		return m_Start.compareTo(o.m_Start);
	}

	
	
	
	
	
}