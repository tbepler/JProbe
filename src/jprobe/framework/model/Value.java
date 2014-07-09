package jprobe.framework.model;

public interface Value<T> {
	
	public Class<T> getType(); 
	public T get() throws Exception;
	
}
