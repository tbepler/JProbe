package util.tuple;

import java.io.Serializable;
import java.util.Arrays;

public class Tuple implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private final Object[] m_Vals;
	private final TupleClass m_Type;
	private final int m_Hash;
	
	public Tuple(Object ... values){
		m_Vals = values.clone();
		m_Type = new TupleClass(m_Vals);
		m_Hash = Arrays.hashCode(m_Vals);
	}
	
	//package protected cast constructor
	Tuple(Tuple t, TupleClass castTo){
		m_Vals = t.m_Vals;
		m_Type = castTo;
		m_Hash = t.m_Hash;
	}
	
	public final int size(){
		return m_Vals.length;
	}
	
	@SuppressWarnings("unchecked")
	public final <T> T get(int index){
		return (T) m_Vals[index];
	}
	
	public final TupleClass getType(){
		return m_Type;
	}
	
	public final Object[] toArray(){
		return m_Vals.clone();
	}
	
	@Override
	public final boolean equals(Object o){
		if(o == null) return false;
		if(o == this) return true;
		if(o instanceof Tuple){
			Tuple other = (Tuple) o;
			if(other.m_Vals.length == this.m_Vals.length){
				for(int i=0; i<m_Vals.length; ++i){
					if(!equals(m_Vals[i], other.m_Vals[i])){
						return false;
					}
				}
				return true;
			}
		}
		return false;
	}
	
	private static boolean equals(Object a, Object b){
		if(a == b) return true;
		if(a == null || b == null) return false;
		return a.equals(b);
	}
	
	@Override
	public int hashCode(){
		return m_Hash;
	}
	
	@Override
	public String toString(){
		StringBuilder builder = new StringBuilder();
		builder.append("(");
		boolean first = true;
		for(Object o : m_Vals){
			if(first){
				builder.append(o);
				first = false;
			}else{
				builder.append(", ").append(o);
			}
		}
		builder.append(")");
		return builder.toString();
	}
	
	
	
	
}
