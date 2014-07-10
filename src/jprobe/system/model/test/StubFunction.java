package jprobe.system.model.test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import jprobe.framework.model.Function;
import jprobe.framework.model.MissingArgsException;
import jprobe.framework.model.Parameter;
import jprobe.framework.model.Value;
import jprobe.system.model.ChildFunction;

public abstract class StubFunction<R> implements Function<R>{
	private static final long serialVersionUID = 1L;
	
	private final Class<R> m_Type;
	private final List<Parameter<?>> m_Params;
	
	public StubFunction(Class<R> type, Parameter<?> ... params){
		m_Type = type;
		m_Params = Arrays.asList(params);
	}
	
	@Override
	public List<? extends Parameter<?>> getParameters() {
		return Collections.unmodifiableList(m_Params);
	}

	@Override
	public Class<R> returnType() {
		return m_Type;
	}

	@Override
	public <T> Function<R> putArgument(Parameter<T> param, Function<T> arg) {
		return new ChildFunction<R,T>(this, param, arg);
	}

	@Override
	public R call() throws MissingArgsException, ExecutionException {
		return this.call(new HashMap<Parameter<?>, Value<?>>());
	}

}
