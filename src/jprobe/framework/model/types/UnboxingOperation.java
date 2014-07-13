package jprobe.framework.model.types;

import jprobe.framework.model.function.InvocationException;
import jprobe.framework.model.function.Procedure;
import jprobe.framework.model.function.TypeMismatchException;

public class UnboxingOperation<R> implements Procedure<R>{
	private static final long serialVersionUID = 1L;

	private final Procedure<R> m_Parent;
	private final TupleClass m_Unbox;
	private final int m_StartIndex;
	
	public UnboxingOperation(Procedure<R> parent, int startIndex, int length){
		m_Parent = parent;
		m_StartIndex = startIndex;
		Signature<R> parentSign = m_Parent.getType();
		Type<?>[] parentParams = parentSign.getParameterTypes();
		Type<?>[] toTuple = new Type<?>[length];
		System.arraycopy(parentParams, startIndex, toTuple, 0, toTuple.length);
		m_Unbox = new TupleClass(toTuple);
				
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

	@Override
	public int numParameters() {
		// TODO Auto-generated method stub
		return 0;
	}

}
