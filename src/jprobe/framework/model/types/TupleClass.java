package jprobe.framework.model.types;

import java.util.Arrays;

import util.ArrayUtils;
import jprobe.framework.model.tuple.Tuple;
import jprobe.framework.model.tuple.Tuple2;

public final class TupleClass implements Type<Tuple>{
	private static final long serialVersionUID = 1L;
	
	private final Type<?>[] m_Types;
	private final int m_Hash;
	
	public TupleClass(Type<?> ... types){
		m_Types = types.clone();
		m_Hash = this.computeHash();
	}
	
	public TupleClass(Object ... objs){
		m_Types = new Type[objs.length];
		for(int i=0; i<m_Types.length; ++i){
			m_Types[i] = Types.typeOf(objs[i]);
		}
		m_Hash = this.computeHash();
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
	
	@SuppressWarnings("unchecked")
	@Override
	public Tuple2<Tuple,Object[]> cast(Object ... objs) {
		if(obj == null) return null;
		if(this.isInstance(obj)){
			return (Tuple) obj;
		}
		throw new ClassCastException("Object: "+obj+" of type: "+Types.typeOf(obj)+" cannot be cast to type: "+this);
	}
	
	@Override
	public final Tuple2<Boolean,Type<?>[]> isAssignableFrom(Type<?> ... types){
		if(types == null || types.length == 0) return resultTuple(false, 0, types);
		Type<?> head = types[0];
		if(head == this) return resultTuple(true, 1, types);
		if(head instanceof TupleClass){
			TupleClass type = (TupleClass) head;
			return resultTuple(arrayAssignableFrom(m_Types, type.m_Types), 1, types);
		}
		return false;
	}
	
	private static <T> Tuple2<Boolean, T[]> resultTuple(boolean result, int used, T ... array){
		if(result){
			return new Tuple2<Boolean, T[]>(result, array);
		}else{
			return new Tuple2<Boolean, T[]>(result, ArrayUtils.tail(used, array));
		}
	}
	
	private static boolean arrayAssignableFrom(Type<?>[] array, Type<?>[] assignableFrom){
		if(array.length == assignableFrom.length){
			for(int i=0; i<array.length; ++i){
				if(!isAssignableFrom(array[i], assignableFrom[i])){
					return false;
				}
			}
			return true;
		}
		return false;
	}
	
	private static boolean isAssignableFrom(Type<?> type, Type<?> assignableFrom){
		if(type == assignableFrom) return true;
		if(type == null) return false;
		return type.isAssignableFrom(assignableFrom).first();
	}
	
	@Override
	public final boolean isInstance(Object o){
		Type<?> type = Types.typeOf(o);
		return this.isTypeInstance(type);
	}
	
	@Override
	public boolean isTypeInstance(Type<?> other) {
		if(other == null) return false;
		if(other == this) return true;
		if(other instanceof TupleClass){
			TupleClass type = (TupleClass) other;
			return arrayTypeInstance(m_Types, type.m_Types);
		}
		return false;
	}
	
	private static boolean arrayTypeInstance(Type<?>[] array, Type<?>[] instance){
		if(array.length == instance.length){
			for(int i=0; i<array.length; ++i){
				if(!isTypeInstance(array[i], instance[i])){
					return false;
				}
			}
			return true;
		}
		return false;
	}
	
	private static boolean isTypeInstance(Type<?> type, Type<?> instance){
		if(type == instance) return true;
		if(type == null) return false;
		return type.isTypeInstance(instance);
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

	
	
	
}
