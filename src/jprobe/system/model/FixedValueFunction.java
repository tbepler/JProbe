package jprobe.system.model;

import jprobe.framework.model.Function;
import jprobe.framework.model.InvocationException;
import jprobe.framework.model.Parameter;
import jprobe.framework.model.TypeMismatchException;
import jprobe.framework.model.Value;

public class FixedValueFunction<R> implements Function<R>{
	private static final long serialVersionUID = 1L;
	
	private static final Parameter<?>[] EMPTY_ARRAY = new Parameter<?>[]{};
	
	private final Value<R> m_Value;
	
	public FixedValueFunction(Value<R> val){
		m_Value = val;
	}
	
	public FixedValueFunction(final R val){
		m_Value = new Value<R>(){

			@SuppressWarnings("unchecked")
			@Override
			public Class<? extends R> getType() {
				return (Class<? extends R>) val.getClass();
			}

			@Override
			public R get() throws Exception {
				return val;
			}
			
		};
	}
	
	@Override
	public Parameter<?>[] getParameters() {
		return EMPTY_ARRAY;
	}

	@Override
	public Class<? extends R> returnType() {
		return m_Value.getType();
	}

	@Override
	public <T> Function<R> putArgument(int paramIndex, Function<T> arg)
			throws TypeMismatchException {
		throw new IndexOutOfBoundsException();
	}

	@Override
	public <T> Function<R> putArgument(int paramIndex, Value<T> arg)
			throws TypeMismatchException {
		throw new IndexOutOfBoundsException();
	}
	
	@Override
	public <T> Function<R> putArgument(int paramIndex, T arg)
			throws TypeMismatchException {
		throw new IndexOutOfBoundsException();
	}

	@Override
	public R invoke(Value<?>... args) throws IllegalArgumentException,
			TypeMismatchException, InvocationException {
		Parameters.checkArguments(this, EMPTY_ARRAY, args);
		try {
			return m_Value.get();
		} catch (Exception e) {
			throw new InvocationException(e);
		}
	}


}
