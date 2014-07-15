package jprobe.framework.model.function;

import java.io.Serializable;

import jprobe.framework.model.types.Signature;
import jprobe.framework.model.types.Typed;


public interface Procedure<R> extends Typed<Procedure<? extends R>>, Serializable{
	
	@Override
	public Signature<R> getType();
	
	public int numParameters();

	public abstract R invoke(Object ... args)
			throws IllegalArgumentException, TypeMismatchException, InvocationException;
}
