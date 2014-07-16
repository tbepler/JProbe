package jprobe.framework.model.types;

import java.util.Arrays;
import java.util.Deque;

import util.ArrayUtils;
import jprobe.framework.model.function.Procedure;
import jprobe.framework.model.tuple.Tuple;

public class TupleClass<T extends Tuple> implements Type<T>{
	private static final long serialVersionUID = 1L;
	
	private final Type<?>[] m_Types;
	private final int m_Hash;
	
	public TupleClass(Type<?> ... types){
		m_Types = types.clone();
		m_Hash = this.computeHash();
	}
	
	public TupleClass(Object ... objs){
		this(Types.typesOf(objs));
	}
	
	private int computeHash(){
		return Arrays.hashCode(m_Types);
	}
	
	public final int size(){
		return m_Types.length;
	}
	
	public final Type<?> get(int index){
		return m_Types[index];
	}
	
	public final Type<?>[] toArray(){
		return m_Types.clone();
	}
	
	@Override
	public final boolean isAssignableFrom(Type<?> type){
		if(type == null) return false;
		if(type == this) return true;
		if(type instanceof TupleClass){
			TupleClass<?> clazz = (TupleClass<?>) type;
			return Types.isAssignableFrom(m_Types, clazz.m_Types);
		}
		return false;
	}
	
	@Override
	public final boolean isInstance(Object o){
		if(o == null) return false;
		Type<?> type = Types.typeOf(o);
		return this.isAssignableFrom(type);
	}
	
	@Override
	public Tuple cast(Object obj){
		if(obj == null) return null;
		if(this.isInstance(obj)){
			return (Tuple) obj;
		}
		throw new ClassCastException("Object: "+obj+" of type: "+type+" cannot be cast to type: "+this);
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
		for(Type<?> type : m_Types){
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

	@Override
	public boolean isBoxable() {
		return true;
	}

	@Override
	public int boxSize() {
		return m_Types.length;
	}

	@Override
	public Type<?>[] unbox() {
		return m_Types.clone();
	}

	@Override
	public Object[] unbox(Tuple obj) {
		return obj.toArray();
	}

	@Override
	public Tuple box(Object[] objs) {
		return new Tuple();
	}

	@Override
	public boolean canBox(Type<?>[] types) {
		// TODO Auto-generated method stub
		return false;
	}

	
	
	
}
