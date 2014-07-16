package jprobe.system.model;

import jprobe.framework.model.function.InvocationException;
import jprobe.framework.model.function.Procedure;
import jprobe.framework.model.function.Signature;
import jprobe.framework.model.function.TypeMismatchException;

public class DerivedFunction<R> implements Procedure<R>{
	private static final long serialVersionUID = 1L;

	@Override
	public Signature<R> getType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int numParameters() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public R invoke(Object... args) throws IllegalArgumentException,
			TypeMismatchException, InvocationException {
		// TODO Auto-generated method stub
		return null;
	}

}
