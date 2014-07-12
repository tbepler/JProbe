package jprobe.framework.model.types;

import jprobe.framework.model.function.Procedure;

public final class Signature<R> implements Type<Procedure<? extends R>>{
	private static final long serialVersionUID = 1L;
	
	private final Type<?>[] m_Params;
	private final Type<? extends R> m_ReturnType;
	
	public Signature(Type<? extends R> returnType, Type<?> ... params){
		m_ReturnType = returnType;
		m_Params = params.clone();
	}
	
	public Type<?>[] getParameterTypess(){
		return m_Params.clone();
	}
	
	/**
	 * Returns the {@link Type} of this signature's return value
	 * 
	 * @return - type of this signatures return value
	 */
	public Type<? extends R> getReturnType(){
		return m_ReturnType;
	}
	
	@Override
	public Procedure<? extends R> cast(Object obj) {
		//TODO
		
	}
		
	@Override
	public String toString(){
		StringBuilder builder = new StringBuilder();
		builder.append(this.getReturnType());
		if(m_Params.length > 0){
			builder.append(" ");
			builder.append(arrayToString(m_Params));
		}
		return builder.toString();
	}
	
	public static <T> String arrayToString(T[] array){
		StringBuilder builder = new StringBuilder();
		builder.append("(");
		boolean first = true;
		for(T o : array){
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
	
	@Override
	public final boolean isAssignableFrom(Type<?> type){
		if(type == null) return false;
		if(type == this) return true;
		if(type instanceof Signature){
			Signature<?> other = (Signature<?>) type;
			if(this.getReturnType().isAssignableFrom(other.getReturnType())){
				return signaturesAssignableFrom(other.m_Params, m_Params);
			}
		}
		return false;
	}
	
	private static boolean signaturesAssignableFrom(Type<?>[] array, Type<?>[] assignableFrom){
		if(array == assignableFrom) return true;
		if((array == null || array.length == 0) && (assignableFrom == null || assignableFrom.length == 0)){
			return true;
		}
		if(array == null || assignableFrom == null) return false;
		if(array.length == assignableFrom.length){
			for(int i=0; i<array.length; i++){
				if(!array[i].isAssignableFrom(assignableFrom[i])){
					return false;
				}
			}
			return true;
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
				return arraysEqual(other.m_Params, m_Params);
			}
		}
		return false;
	}
	
	private static <T> boolean arraysEqual(T[] params1, T[] params2){
		if(params1.length == params2.length){
			for(int i=0; i<params1.length; i++){
				if(!params1[i].equals(params2[i])){
					return false;
				}
			}
			return true;
		}
		return false;
	}
	
}
