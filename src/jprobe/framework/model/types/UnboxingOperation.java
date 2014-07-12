package jprobe.framework.model.types;

import jprobe.framework.model.function.InvocationException;
import jprobe.framework.model.function.Procedure;
import jprobe.framework.model.function.TypeMismatchException;

public class UnboxingOperation<R> implements Procedure<R>{
	private static final long serialVersionUID = 1L;

	private final Procedure<R> m_Parent;
	private final TupleClass<?> m_Unbox;
	
	public UnboxingOperation(Procedure<R> parent, TupleClass<?> unboxType){
		m_Parent = parent;
		m_Unbox = unboxType;
		
	}
	
	@Override
	public Signature<R> getType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public R invoke(Procedure<?>... args) throws IllegalArgumentException,
			TypeMismatchException, InvocationException {
		// TODO Auto-generated method stub
		return null;
	}

}
