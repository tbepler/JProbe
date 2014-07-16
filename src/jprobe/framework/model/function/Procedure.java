package jprobe.framework.model.function;

import java.io.Serializable;

import jprobe.framework.model.types.Typed;


public interface Procedure<R> extends Serializable{
	
	public int numParameters();

	public abstract R invoke(Object ... args)
			throws IllegalArgumentException, TypeMismatchException, InvocationException;
}
