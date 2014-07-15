package jprobe.framework.model.types;

import util.ArrayUtils;
import jprobe.framework.model.function.InvocationException;
import jprobe.framework.model.function.Procedure;
import jprobe.framework.model.function.TypeMismatchException;

public class ProcedureAdapter<R> implements Procedure<R>{
	private static final long serialVersionUID = 1L;

	private final Procedure<?> m_Parent;
	private final Signature<R> m_Sign;
	
	public ProcedureAdapter(Procedure<?> adapt, Signature<R> sign){
		m_Parent = adapt;
		m_Sign = sign;
	}
	
	@Override
	public Signature<R> getType() {
		return m_Sign;
	}

	@Override
	public int numParameters() {
		return m_Sign.size();
	}

	@Override
	public R invoke(Object ... args) throws IllegalArgumentException,
			TypeMismatchException, InvocationException {
		Signature<?> adapt = m_Parent.getType();
		Object[] extracted;
		try{
			extracted = Types.extract(adapt.getParameterTypes(), ArrayUtils.toDeque(args));
		}catch(RuntimeException e){
			throw new IllegalArgumentException(e);
		}
		Object val = m_Parent.invoke(extracted);
		try{
			return m_Sign.getReturnType().extract(val);
		}catch(RuntimeException e){
			throw new TypeMismatchException(e);
		}
	}

}
