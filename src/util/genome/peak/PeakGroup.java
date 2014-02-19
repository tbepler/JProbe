package util.genome.peak;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.lang3.builder.HashCodeBuilder;

public class PeakGroup implements Serializable, Iterable<Peak>{
	private static final long serialVersionUID = 1L;
	
	public static final String[][] FORMATS = Parser.FORMATS;
	
	public static PeakGroup parsePeakGroup(Scanner s){
		return Parser.parsePeakGroup(s);
	}
	
	private final List<Peak> m_Peaks = new ArrayList<Peak>();
	private final int m_Hash;
	
	public PeakGroup(){
		m_Hash = this.computeHash();
	}
	
	public PeakGroup(List<Peak> peaks){
		for(Peak p : peaks){
			m_Peaks.add(p);
		}
		m_Hash = this.computeHash();
	}
	
	private int computeHash(){
		HashCodeBuilder builder = new HashCodeBuilder();
		for(Peak p : this){
			builder.append(p);
		}
		return builder.toHashCode();
	}
	
	@Override
	public int hashCode(){
		return m_Hash;
	}
	
	@Override
	public String toString(){
		String s = "";
		for(Peak p : m_Peaks){
			s += p.toString() + "\n";
		}
		return s;
	}
	
	@Override
	public boolean equals(Object o){
		if(o == null) return false;
		if(o == this) return true;
		if(o instanceof PeakGroup){
			PeakGroup other = (PeakGroup) o;
			if(this.size() == other.size()){
				for(int i=0; i<this.size(); i++){
					if(!this.getPeak(i).equals(other.getPeak(i))) return false;
				}
				return true;
			}
			return false;
		}
		return false;
	}
	
	public boolean contains(Peak p){
		return m_Peaks.contains(p);
	}
	
	public Peak getPeak(int index){
		return m_Peaks.get(index);
	}
	
	public int size(){
		return m_Peaks.size();
	}

	@Override
	public Iterator<Peak> iterator() {
		return m_Peaks.iterator();
	}
	
}
