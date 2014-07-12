package jprobe.framework.model.function;

import java.io.Serializable;

import jprobe.framework.model.types.Signature;


public interface Procedure<R> extends Serializable{
	
	public Signature getSignature();

	public abstract R invoke(Function<?> ... args)
			throws IllegalArgumentException, TypeMismatchException, InvocationException;
}
