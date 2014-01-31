package plugins.genome.services.utils;

import java.io.Serializable;

public class Chromosome implements Comparable<Chromosome>, Serializable{
	private static final long serialVersionUID = 1L;
	
	private final String m_Id;
	
	public Chromosome(String id){
		m_Id = id.trim().toLowerCase();
	}
	
	@Override
	public String toString(){
		return m_Id;
	}
	
	@Override
	public int hashCode(){
		return m_Id.hashCode();
	}
	
	@Override
	public boolean equals(Object o){
		if(o == null) return false;
		if(this == o) return true;
		if(o instanceof Chromosome){
			return m_Id.equals(((Chromosome) o).m_Id);
		}
		return false;
	}

	@Override
	public int compareTo(Chromosome o) {
		return m_Id.compareTo(o.m_Id);
	}
	
}
