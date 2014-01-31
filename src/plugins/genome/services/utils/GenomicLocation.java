package plugins.genome.services.utils;

import java.io.Serializable;

import org.apache.commons.lang3.builder.HashCodeBuilder;

public class GenomicLocation implements Comparable<GenomicLocation>, Serializable{
	private static final long serialVersionUID = 1L;
	
	private static final String PREFIX = "Chr";
	private static final char SEP = ':';
	
	public static GenomicLocation parseString(String s){
		try{
			String chr = s.substring(0, s.indexOf(SEP));
			if(chr.startsWith(PREFIX)){
				chr = chr.substring(PREFIX.length());
			}
			long baseIndex = Long.parseLong(s.substring(s.indexOf(SEP)+1));
			return new GenomicLocation(chr, baseIndex);
		} catch (Exception e){
			return null;
		}
	}
	
	private final Chromosome m_Chr;
	private final long m_BaseIndex;
	private final int m_Hash;
	
	public GenomicLocation(Chromosome chr, long baseIndex){
		m_Chr = chr;
		m_BaseIndex = baseIndex;
		m_Hash = this.computeHash();
	}
	
	public GenomicLocation(Chromosome chr, int start){
		this(chr, (long) start); 
	}
	
	public GenomicLocation(String chr, long start){
		this(new Chromosome(chr), start);
	}
	
	public GenomicLocation(String chr, int start){
		this(chr, (long) start);
	}
	
	private int computeHash(){
		return new HashCodeBuilder(11, 461).append(m_Chr).append(m_BaseIndex).toHashCode();
	}
	
	public Chromosome getChromosome(){
		return m_Chr;
	}
	
	public long getBaseIndex(){
		return m_BaseIndex;
	}
	
	@Override
	public String toString(){
		return PREFIX+m_Chr+SEP+m_BaseIndex;
	}
	
	@Override
	public boolean equals(Object o){
		if(o == null) return false;
		if(this == o) return true;
		if(o instanceof GenomicLocation){
			GenomicLocation other = (GenomicLocation) o;
			return m_Chr.equals(other.m_Chr) && m_BaseIndex == other.m_BaseIndex;
		}
		return false;
	}
	
	@Override
	public int hashCode(){
		return m_Hash;
	}

	@Override
	public int compareTo(GenomicLocation o) {
		if(m_Chr.equals(o.m_Chr)){
			if(m_BaseIndex < o.m_BaseIndex){
				return -1;
			}
			if(m_BaseIndex > o.m_BaseIndex){
				return 1;
			}
			return 0;
		}
		return m_Chr.compareTo(o.m_Chr);
	}
	
	
	
	
}
