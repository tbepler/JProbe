package jprobe.framework.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public interface Function<R> extends Serializable{
	
	public List<? extends Parameter<?>> getParameters();
	
	public Class<R> returnType();
	
	/*
	 * One parameter - one function
	 * 
	 */
	public <T> Function<R> putArgument(Parameter<T> param, Function<T> arg);
	
	public R call() throws MissingArgsException, ExecutionException;
	
	public R call(Map<Parameter<?>, Value<?>> args) throws MissingArgsException, ExecutionException;
	

}
