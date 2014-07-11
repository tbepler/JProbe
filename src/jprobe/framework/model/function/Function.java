package jprobe.framework.model.function;


public abstract class Function<R> extends Procedure<R>{
	private static final long serialVersionUID = 1L;
	
	/*
	 * One parameter - one function
	 * 
	 */
	public abstract <T> Function<R> putArgument(int paramIndex, Function<T> arg)
			throws TypeMismatchException;
	
	public abstract Function<R> putArguments(int[] indices, Function<?>[] args)
			throws TypeMismatchException;
	
	public abstract <T> Function<R> putArgument(int paramIndex, T arg)
			throws TypeMismatchException;
	
	public abstract Function<R> putArguments(int[] indices, Object[] args)
			throws TypeMismatchException;
	

}
