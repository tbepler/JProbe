package jprobe.system.model;

import util.tuple.TupleClass;
import jprobe.framework.model.function.Signature;

public class DefaultSignature extends Signature{
	private static final long serialVersionUID = 1L;
	
	private static final Signature[] EMPTY = new Signature[]{};
	
	private final Signature[] m_Params;
	private final Signature[] m_Return;
	
	public DefaultSignature(Signature[] returnType, Signature ... params){
		if(returnType != null){
			m_Return = returnType;
		}else{
			m_Return = EMPTY;
		}
		if(params != null){
			m_Params = params.clone();
		}else{
			m_Params = EMPTY;
		}
	}
	
	public DefaultSignature(Class<?> returnType, Signature ... params){
		this(new, params);
	}
	
	public DefaultSignature(TupleClass returnType){
		this(returnType, EMPTY);
	}
	
	public DefaultSignature(Class<?> returnType){
		this(returnType, EMPTY);
	}

	@Override
	public Signature[] getParameters() {
		return m_Params;
	}

	@Override
	public TupleClass getReturnType() {
		return m_Return;
	}

}
