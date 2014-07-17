package jprobe.framework.model.tuple;

import java.io.Serializable;

import jprobe.framework.model.Pointer;

public class PointerTuple implements Serializable{
	private static final long serialVersionUID = 1L;

	private final Pointer[] m_Vals;
	
	public PointerTuple(Pointer ... vals){
		m_Vals = vals.clone();
	}
	
	public <T> T get(int index){
		return m_Vals[index].get();
	}
	
	public Pointer[] toArray(){
		return m_Vals.clone();
	}
	
	public int size(){
		return m_Vals.length;
	}
	
}
