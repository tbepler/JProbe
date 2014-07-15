package jprobe.framework.model.types;

import java.util.Deque;

import jprobe.framework.model.function.Procedure;

public final class Signature<R> implements Type<Procedure<? extends R>>{
	private static final long serialVersionUID = 1L;
	
	private final TupleClass m_Params;
	private final Type<? extends R> m_ReturnType;
	
	public Signature(Type<? extends R> returnType, Type<?> ... params){
		m_ReturnType = returnType;
		m_Params = Types.asTupleType(params);
	}
	
	public Type<?>[] getParameterTypes(){
		return m_Params.toArray();
	}
	
	public Type<?> getParameterType(int index){
		return m_Params.get(index);
	}
	
	public int size(){
		return m_Params.size();
	}
	
	/**
	 * Returns the {@link Type} of this signature's return type
	 * 
	 * @return - type of this signatures return value
	 */
	public Type<? extends R> getReturnType(){
		return m_ReturnType;
	}
	
	@Override
	public Procedure<? extends R> extract(Deque<Object> objs) {
		if(objs == null || objs.isEmpty()){
			throw new RuntimeException("Unable to extract type: "+this+" from an empty deque.");
		}
		try{
			Procedure<? extends R> extracted = this.extract(objs.peek());
			objs.poll();
			return extracted;
		}catch(RuntimeException e){
			return new AdapterOperation<R>(m_ReturnType.extract(objs), this);
		}
	}
	
	public Procedure<? extends R> extract(Object obj){
		if(obj == null) return null;
		Type<?> type = Types.typeOf(obj);
		if(this.isExtractableFrom(type)){
			if(obj instanceof Procedure){
				Procedure<?> proc = (Procedure<?>) obj;
				return new ProcedureAdapter<R>(proc, this);
			}
			return new AdapterOperation<R>(m_ReturnType.extract(obj),this);
		}
		throw new ClassCastException("Object "+obj+" of type: "+type+" cannot be cast to type: "+this);
	}

	@Override
	public boolean isExtractableFrom(Deque<Type<?>> types) {
		if(types == null || types.isEmpty()) return false;
		Type<?> head = types.poll();
		if(this.isExtractableFrom(head)){
			return true;
		}
		types.push(head);
		//if this signature doesn't take parameters, then check if
		//the types could be boxed into this signature's return type
		return m_Params.size() == 0 && m_ReturnType.isExtractableFrom(types);
	}
	
	@Override
	public boolean isExtractableFrom(Type<?> type){
		if(type == null) return false;
		if(type == this) return true;
		if(type instanceof Signature){
			Signature<?> other = (Signature<?>) type;
			if(this.getReturnType().isExtractableFrom(other.getReturnType())){
				return other.m_Params.isExtractableFrom(m_Params);
			}
			return false;
		}
		//if this doesn't require params, check if the given type can be
		//boxed into this signature's return type
		return m_Params.size() == 0 && m_ReturnType.isExtractableFrom(type);
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
	
	@SuppressWarnings("unchecked")
	@Override
	public Procedure<? extends R> cast(Object obj) {
		if(obj == null) return null;
		Type<?> type = Types.typeOf(obj);
		if(this.isAssignableFrom(type)){
			return (Procedure<? extends R>) obj;
		}
		throw new ClassCastException("Object "+obj+" of type: "+type+" cannot be cast to type: "+this);
	}
		
	@Override
	public String toString(){
		StringBuilder builder = new StringBuilder();
		builder.append(m_Params).append(" => ");
		builder.append(this.getReturnType());
		return builder.toString();
	}
	
	@Override
	public final boolean isAssignableFrom(Type<?> type){
		if(type == null) return false;
		if(type == this) return true;
		if(type instanceof Signature){
			Signature<?> other = (Signature<?>) type;
			if(this.getReturnType().isAssignableFrom(other.getReturnType())){
				return other.m_Params.isAssignableFrom(m_Params);
			}
		}
		return false;
	}

	@Override
	public boolean isInstance(Object obj) {
		Type<?> type = Types.typeOf(obj);
		return this.isAssignableFrom(type);
	}
	
	@Override
	public final boolean equals(Object o){
		if(o == null) return false;
		if(o == this) return true;
		if(o instanceof Signature){
			Signature<?> other = (Signature<?>) o;
			if(this.getReturnType().equals(other.getReturnType())){
				return other.m_Params.equals(m_Params);
			}
		}
		return false;
	}

}
