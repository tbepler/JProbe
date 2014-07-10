package jprobe.system.model;

import jprobe.framework.model.Function;
import jprobe.framework.model.Value;

public class FunctionValue<T> implements Value<T>{
	
	private final Function<T> m_Func;
	private final Value<?>[] m_Args;
	
	public FunctionValue(Function<T> function, Value<?> ... args){
		m_Func = function;
		m_Args = args;
	}
	
	@Override
	public Class<? extends T> getType() {
		return m_Func.returnType();
	}

	@Override
	public T get() throws Exception {
		return m_Func.invoke(m_Args);
	}

}
