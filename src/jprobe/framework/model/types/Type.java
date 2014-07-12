package jprobe.framework.model.types;

import java.io.Serializable;

public abstract class Type<T> implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private final Types m_Type;
	
	Type(Types t){
		m_Type = t;
	}
	
	public Types getType(){
		return m_Type;
	}
	
	public abstract T cast(Object obj);
	
	public abstract boolean isAssignableFrom(Type<?> other);
	
	public abstract boolean isInstance(Object obj);
	
}
