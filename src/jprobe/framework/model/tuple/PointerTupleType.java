package jprobe.framework.model.tuple;

import java.util.Arrays;
import java.util.Deque;

import jprobe.framework.model.Pointer;
import jprobe.framework.model.types.Type;
import jprobe.framework.model.types.Types;

public class PointerTupleType implements Type<PointerTuple>{
	private static final long serialVersionUID = 1L;
	
	private final Type<?>[] m_Types;
	private final int m_Hash;
	
	public PointerTupleType(Type<?> ... types){
		m_Types = types.clone();
		m_Hash = Arrays.hashCode(types);
	}
	
	public final int size(){
		return m_Types.length;
	}
	
	public final <U> Type<? extends U> get(int index){
		return (Type<? extends U>) m_Types[index];
	}
	
	public final Type<?>[] toArray(){
		return m_Types.clone();
	}
	
	@Override
	public final boolean isAssignableFrom(Type<?> type){
		if(type == null) return false;
		if(type == this) return true;
		if(type instanceof PointerTupleType){
			PointerTupleType clazz = (PointerTupleType) type;
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
	public PointerTuple cast(Object obj){
		if(obj == null) return null;
		if(this.isInstance(obj)){
			return (PointerTuple) obj;
		}
		throw new ClassCastException("Object: "+obj+" of type: "+Types.typeOf(obj)+" cannot be cast to type: "+this);
	}
	
	@Override
	public int hashCode(){
		return m_Hash;
	}
	
	@Override
	public final boolean equals(Object o){
		if(o == null) return false;
		if(o == this) return true;
		if(o instanceof PointerTupleType){
			PointerTupleType other = (PointerTupleType) o;
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
	public Type<?>[] unbox() {
		return m_Types.clone();
	}

	@Override
	public Pointer[] unbox(PointerTuple obj) {
		return obj.toArray();
	}

	@Override
	public PointerTuple box(Deque<Pointer> objs) {
		Pointer[] array = new Pointer[this.size()];
		for(int i=0; i<array.length; ++i){
			array[i] = objs.pop();
		}
		return new PointerTuple(array);
	}

	@Override
	public boolean canBox(Deque<Type<?>> types) {
		if(types.size() < this.size()){
			return false;
		}
		Type<?>[] array = new Type<?>[this.size()];
		for(int i=0; i<array.length; ++i){
			array[i] = types.pop();
		}
		return Types.isAssignableFrom(m_Types, array);
	}

}
