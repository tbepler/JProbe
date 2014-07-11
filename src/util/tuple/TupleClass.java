package util.tuple;

import java.io.Serializable;
import java.util.Arrays;

public final class TupleClass implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private final Class<?>[] m_Types;
	private final int m_Hash;
	
	public TupleClass(Class<?> ... classes){
		m_Types = classes.clone();
		m_Hash = this.computeHash();
	}
	
	public TupleClass(Object ... objs){
		m_Types = new Class<?>[objs.length];
		for(int i=0; i<m_Types.length; ++i){
			m_Types[i] = objs[i] == null ? null : objs[i].getClass();
		}
		m_Hash = this.computeHash();
	}
	
	private int computeHash(){
		return Arrays.hashCode(m_Types);
	}
	
	public final int size(){
		return m_Types.length;
	}
	
	public final Class<?> get(int index){
		return m_Types[index];
	}
	
	public final Class<?>[] toArray(){
		return m_Types.clone();
	}
	
	public Tuple cast(Tuple t){
		if(t == null) return t;
		if(this.isInstance(t)){
			return new Tuple(t, this);
		}
		throw new TupleCastException("Tuple "+t+" of type "+t.getType()+" cannot be cast to type "+this);
	}
	
	public final boolean isInstance(Tuple t){
		if(t == null) return false;
		if(t.getType() == this) return true;
		if(t.size() == m_Types.length){
			for(int i=0; i<m_Types.length; ++i){
				if(!isInstance(m_Types[i], t.get(i))){
					return false;
				}
			}
			return true;
		}
		return this.isAssignableFrom(t.getType());
	}
	
	private static boolean isInstance(Class<?> clazz, Object obj){
		if(obj == null) return true;
		if(clazz == null) return false;
		return clazz.isInstance(obj);
	}
	
	public final boolean isAssignableFrom(TupleClass other){
		if(other == null) return false;
		if(other == this) return true;
		if(other.m_Types.length == m_Types.length){
			for(int i=0; i<m_Types.length; ++i){
				if(!isAssignableFrom(m_Types[i], other.m_Types[i])){
					return false;
				}
			}
			return true;
		}
		return false;
	}
	
	private static boolean isAssignableFrom(Class<?> clazz, Class<?> assignableFrom){
		if(clazz == assignableFrom || assignableFrom == null) return true;
		if(clazz == null) return false;
		return clazz.isAssignableFrom(assignableFrom);
	}
	
	@Override
	public int hashCode(){
		return m_Hash;
	}
	
	@Override
	public final boolean equals(Object o){
		if(o == null) return false;
		if(o == this) return true;
		if(o instanceof TupleClass){
			TupleClass other = (TupleClass) o;
			if(m_Types.length == other.m_Types.length){
				for(int i=0; i<m_Types.length; ++i){
					if(!equals(m_Types[i], other.m_Types[i])){
						return false;
					}
				}
			}
			return true;
		}
		return false;
	}
	
	private static boolean equals(Object a, Object b){
		if(a == b) return true;
		if(a == null || b == null) return false;
		return a.equals(b);
	}
	
	@Override
	public String toString(){
		StringBuilder builder = new StringBuilder();
		builder.append("(");
		boolean first = true;
		for(Class<?> type : m_Types){
			if(first){
				builder.append(type);
				first = false;
			}else{
				builder.append(", ").append(type);
			}
		}
		builder.append(")");
		return builder.toString();
	}
	
	
	
}
