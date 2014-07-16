package jprobe.framework.model.types;

import java.util.Arrays;
import java.util.Deque;

import util.ArrayUtils;
import jprobe.framework.model.function.Procedure;
import jprobe.framework.model.tuple.Tuple;

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
	
	@Override
	public Tuple extract(Deque<Object> objs) {
		if(objs == null || objs.size() == 0){
			throw new RuntimeException("Unable to extract type: "+this+" from an empty deque.");
		}
		Object obj = objs.poll();
		if(this.isExtractableFrom(Types.typeOf(obj))){
			try{
				return this.extract(obj);
			}catch(RuntimeException e){
				objs.push(obj);
				throw e;
			}
		}
		objs.push(obj);
		//try boxing the given types into this tuple type
		return new Tuple(Types.extract(m_Types, objs));

	}
	
	@Override
	public Tuple extract(Object obj){
		if(obj == null) return null;
		Type<?> type = Types.typeOf(obj);
		if(this.isAssignableFrom(type)){
			return this.cast(obj);
		}
		if(this.canUnwrap(type)){
			return this.unwrap(obj);
		}

		throw new ClassCastException("Object: "+obj+" of type: "+type+" cannot be cast to type: "+this);
	}
	
	@Override
	public boolean canExtract(Deque<Object> objs) {
		Deque<Type<?>> types = Types.typesOf(objs);
		return this.isExtractableFrom(types);
	}
	
	@Override
	public boolean canExtract(Object obj){
		Type<?> type = Types.typeOf(obj);
		return this.isExtractableFrom(type);
	}
	
	@Override
	public final boolean isExtractableFrom(Deque<Type<?>> types){
		if(types == null || types.size() == 0) return false;
		Type<?> head = types.poll();
		if(this.isExtractableFrom(head)){
			return true;
		}
		types.push(head);
		//test whether the given types can be boxed into a tuple
		//of this type
		return Types.isExctractableFrom(m_Types, types);
	}
	
	@Override
	public final boolean isExtractableFrom(Type<?> type){
		return this.isAssignableFrom(type) || this.canUnwrap(type);
	}
	
	private Tuple unwrap(Object obj){
		if(obj instanceof Procedure){
			Procedure<?> p = (Procedure<?>) obj;
			try {
				return this.extract(ArrayUtils.<Object>toDeque(p.invoke()));
			} catch (Exception e){
				throw new ClassCastException("Object: "+obj+" of type: "+p.getType()+" cannot be cast to type: "+this);
			}
		}
		if(obj instanceof Tuple){
			Tuple t = (Tuple) obj;
			return this.extract(ArrayUtils.toDeque(t.toArray()));
		}
		throw new ClassCastException("Object: "+obj+" of type: "+Types.typeOf(obj)+" cannot be cast to type: "+this);
	}
	
	private boolean canUnwrap(Type<?> type){
		if(type instanceof Signature){
			Signature<?> sign = (Signature<?>) type;
			return sign.size() == 0 && this.isExtractableFrom(sign.getReturnType());
		}
		if(type instanceof TupleClass){
			TupleClass clazz = (TupleClass) type;
			//unbox the tuple type
			Deque<Type<?>> deck = ArrayUtils.toDeque(clazz.m_Types);
			//check if this can be extracted from the unboxed tuple type,
			//for this to be valid, all the elements of the other tuple must be used,
			//therefore, need to also check that the deque is empty after extracting
			return this.isExtractableFrom(deck) && deck.isEmpty();
		}
		return false;
	}
	
	@Override
	public Tuple cast(Object obj){
		if(obj == null) return null;
		Type<?> type = Types.typeOf(obj);
		if(this.isAssignableFrom(type)){
			return (Tuple) obj;
		}
		throw new ClassCastException("Object: "+obj+" of type: "+type+" cannot be cast to type: "+this);
	}
	
	@Override
	public final boolean isAssignableFrom(Type<?> type){
		if(type == null) return false;
		if(type == this) return true;
		if(type instanceof TupleClass){
			TupleClass clazz = (TupleClass) type;
			return Types.isAssignableFrom(m_Types, clazz.m_Types);
		}
		return false;
	}
	
	@Override
	public final boolean isInstance(Object o){
		Type<?> type = Types.typeOf(o);
		return this.isAssignableFrom(type);
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
