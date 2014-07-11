package jprobe.framework.model.function;


public abstract class Procedure<R> extends Signature<R>{
	private static final long serialVersionUID = 1L;

	@Override
	public abstract Parameter<?>[] getParameters();

	public abstract R invoke(Function<?> ... args)
			throws IllegalArgumentException, TypeMismatchException, InvocationException;
}
