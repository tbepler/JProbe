package plugins.genome.util;

import java.io.Serializable;

import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Chromosome implements Comparable<Chromosome>, Serializable{
	private static final long serialVersionUID = 1L;
	
	private final String m_Id;
	private final long m_Size;
	private final GenomicContext m_Context;
	private final int m_HashCode;
	
	Chromosome(GenomicContext context, String id, long size){
		m_Context = context;
		m_Id = id.trim().toLowerCase();
		m_Size = size;
		m_HashCode = new HashCodeBuilder(947, 569).append(m_Id).append(m_Size).append(m_Context).toHashCode();
	}
	
	GenomicContext getGenomicContext(){
		return m_Context;
	}
	
	public Chromosome nextChr(){
		return m_Context.nextChr(this);
	}
	
	public Chromosome prevChr(){
		return m_Context.prevChr(this);
	}
	
	public String getId(){
		return m_Id;
	}
	
	public long getSize(){
		return m_Size;
	}
	
	@Override
	public String toString(){
		return m_Id;
	}
	
	@Override
	public int hashCode(){
		return m_HashCode;
	}
	
	@Override
	public boolean equals(Object o){
		if(o == null) return false;
		if(this == o) return true;
		if(o instanceof Chromosome){
			Chromosome other = (Chromosome) o;
			return m_Id.equals(other.m_Id) && m_Size == other.m_Size && m_Context == other.m_Context;
		}
		return false;
	}

	@Override
	public int compareTo(Chromosome o) {
		return m_Id.compareTo(o.m_Id);
	}
	
}
