package jprobe.framework.model.function;


public interface Function<R> extends Procedure<R>{
	
	/*
	 * One parameter - one function
	 * 
	 */
	public <U> Function<R> putArgument(int paramIndex, Function<U> arg)
			throws TypeMismatchException;
	
	public Function<R> putArguments(int[] indices, Function<?>[] args)
			throws TypeMismatchException;
	
	public <U> Function<R> putArgument(int paramIndex, U arg)
			throws TypeMismatchException;
	
	public Function<R> putArguments(int[] indices, Object[] args)
			throws TypeMismatchException;
	

}
