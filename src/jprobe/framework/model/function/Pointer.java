package jprobe.framework.model.function;

import java.io.Serializable;

import jprobe.framework.model.types.Type;
import jprobe.framework.model.types.Types;

public class Pointer<T> implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private final Thunk<T> m_Thunk;
	private T m_Val;
	private boolean m_Evald;
	
	public Pointer(Thunk<T> thunk){
		m_Thunk = thunk;
		m_Val = null;
		m_Evald = false;
	}
	
	public Pointer(T val){
		m_Thunk = null;
		m_Val = val;
		m_Evald = true;
	}

	public Type<T> getPointerType() {
		if(!m_Evald){
			return m_Thunk.getEvaluationType();
		}
		return Types.typeOf(m_Val);
	}
	
	public T get(){
		if(!m_Evald){
			m_Val = m_Thunk.evaluate();
			m_Evald = true;
		}
		return m_Val;
	}
	
	
	
}
