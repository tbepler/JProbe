package jprobe.framework.model;

import java.util.List;

public interface Function<R>{
	
	public List<Parameter<?>> getParameters();
	
	public Class<R> returnType();
	
	/*
	 * One parameter - one function
	 * 
	 */
	public <T> Function<R> putArgument(Parameter<T> param, Function<T> arg);
	
	public R call();
	

}
