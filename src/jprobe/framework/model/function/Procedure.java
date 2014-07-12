package jprobe.framework.model.function;

import java.io.Serializable;

import jprobe.framework.model.types.Signature;
import jprobe.framework.model.types.Typed;


public interface Procedure<R,T extends Procedure<R,T>> extends Typed<T>, Serializable{
	
	@Override
	public Signature<R,T> getType();

	public abstract R invoke(Function<?,?> ... args)
			throws IllegalArgumentException, TypeMismatchException, InvocationException;
}
