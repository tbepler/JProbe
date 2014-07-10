package jprobe.framework.model;

import java.io.Serializable;

public interface Procedure<R> extends Serializable{
	
	public Parameter<?>[] getParameters();
	
	public Class<? extends R> returnType();

	public R invoke(Value<?> ... args)
			throws IllegalArgumentException, TypeMismatchException, InvocationException;
}
