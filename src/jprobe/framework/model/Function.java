package jprobe.framework.model;

import java.io.Serializable;

public interface Function<R> extends Serializable{
	
	public Parameter<?>[] getParameters();
	
	public Class<? extends R> returnType();
	
	/*
	 * One parameter - one function
	 * 
	 */
	public <T> Function<R> putArgument(int paramIndex, Function<T> arg) throws TypeMismatchException;
	public <T> Function<R> putArgument(int paramIndex, Value<T> arg) throws TypeMismatchException;
	public <T> Function<R> putArgument(int paramIndex, T arg) throws TypeMismatchException;
	
	public R invoke(Value<?> ... args)
			throws IllegalArgumentException, TypeMismatchException, InvocationException;
	

}
