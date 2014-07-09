package jprobe.system.model;

import java.util.Map;

import jprobe.framework.model.Function;
import jprobe.framework.model.Parameter;
import jprobe.framework.model.Value;

public class FunctionValue<T> implements Value<T>{
	
	private final Function<T> m_Func;
	private final Map<Parameter<?>, Value<?>> m_Args;
	
	public FunctionValue(Function<T> function, Map<Parameter<?>, Value<?>> args){
		m_Func = function;
		m_Args = args;
	}
	
	@Override
	public Class<T> getType() {
		return m_Func.returnType();
	}

	@Override
	public T get() throws Exception {
		return m_Func.call(m_Args);
	}

}
