package plugins.genome.services.utils;

import java.io.Serializable;

import org.apache.commons.lang3.builder.HashCodeBuilder;

public class GenomicLocation implements Comparable<GenomicLocation>, Serializable{
	private static final long serialVersionUID = 1L;
	
	private static final String PREFIX = "Chr";
	private static final char SEP = ':';
	
	static GenomicLocation parseString(GenomicContext context, String s) throws ParsingException{
		try{
			String chr = s.substring(0, s.indexOf(SEP));
			if(chr.startsWith(PREFIX)){
				chr = chr.substring(PREFIX.length());
			}
			Chromosome c = context.getChr(chr);
			long baseIndex = Long.parseLong(s.substring(s.indexOf(SEP)+1));
			return new GenomicLocation(context, c, baseIndex);
		} catch (Exception e){
			throw new ParsingException(e);
		}
	}
	
	private final Chromosome m_Chr;
	private final long m_BaseIndex;
	private final GenomicContext m_Context;
	private final int m_Hash;
	
	GenomicLocation(GenomicContext context, Chromosome chr, long baseIndex){
		if(chr.getGenomicContext() != context){
			throw new RuntimeException("Error: chromosome "+chr+" not found in genome "+context);
		}
		if(baseIndex > chr.getSize()){
			throw new RuntimeException("Error: index "+baseIndex+" is out of bounds on chromosome "+chr+"\n"+chr+" is only "+chr.getSize()+" bases long");
		}
		m_Chr = chr;
		m_BaseIndex = baseIndex;
		m_Context = context;
		m_Hash = this.computeHash();
	}
	
	GenomicLocation(GenomicContext context, Chromosome chr, int baseIndex){
		this(context, chr, (long) baseIndex); 
	}
	
	private int computeHash(){
		return new HashCodeBuilder(11, 461).append(m_Chr).append(m_BaseIndex).append(m_Context).toHashCode();
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
			return m_Chr.equals(other.m_Chr) && m_BaseIndex == other.m_BaseIndex && m_Context == other.m_Context;
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
