package jprobe.framework.model.types;

import java.io.Serializable;

public interface Type<T> extends Serializable{
	
	public T cast(Object obj);
	
	public boolean isAssignableFrom(Type<?> other);
	
	public boolean isInstance(Object obj);
	
}
