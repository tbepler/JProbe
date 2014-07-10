package jprobe.framework.model;

public interface Value<T> {
	
	public Class<? extends T> getType(); 
	public T get() throws Exception;
	
}
