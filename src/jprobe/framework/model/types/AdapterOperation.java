package jprobe.framework.model.types;

import jprobe.framework.model.function.InvocationException;
import jprobe.framework.model.function.Procedure;
import jprobe.framework.model.function.TypeMismatchException;

public class AdapterOperation<R> implements Procedure<R> {
	private static final long serialVersionUID = 1L;
	
	private final R m_Obj;
	private final Signature<R> m_Sign;
	
	public AdapterOperation(R obj, Signature<R> signature){
		m_Obj = obj;
		m_Sign = signature;
	}

	@Override
	public Signature<R> getType() {
		return m_Sign;
	}

	@Override
	public R invoke(Object ... args) throws IllegalArgumentException,
			TypeMismatchException, InvocationException {
		//adapter function simply ignores args and returns its object
		return m_Obj;
	}

	@Override
	public int numParameters() {
		return m_Sign.size();
	}

}
