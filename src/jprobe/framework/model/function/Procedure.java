package jprobe.framework.model.function;

import java.io.Serializable;

import jprobe.framework.model.types.Signature;
import jprobe.framework.model.types.Typed;


public interface Procedure<R> extends Typed<Procedure<? extends R>>, Serializable{
	
	@Override
	public Signature<R> getType();

	public abstract R invoke(Procedure<?> ... args)
			throws IllegalArgumentException, TypeMismatchException, InvocationException;
}
