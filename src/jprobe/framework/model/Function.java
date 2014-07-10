package jprobe.framework.model;

public interface Function<R> extends Procedure<R>{
	
	/*
	 * One parameter - one function
	 * 
	 */
	public <T> Function<R> putArgument(int paramIndex, Function<T> arg) throws TypeMismatchException;
	public <T> Function<R> putArgument(int paramIndex, Value<T> arg) throws TypeMismatchException;
	public <T> Function<R> putArgument(int paramIndex, T arg) throws TypeMismatchException;
	

}
