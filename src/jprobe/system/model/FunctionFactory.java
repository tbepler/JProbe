package jprobe.system.model;

import jprobe.framework.model.Function;
import jprobe.framework.model.Procedure;
import jprobe.framework.model.TypeMismatchException;

public interface FunctionFactory {
	
	public <T> Function<T> newFunction(Procedure<T> procedure);
	
	public <T> Function<T> newFixedValueFunction(T value);
	
	public <T> Function<T> newNullValueFunction(Class<T> clazz);
	
	public <T,U> Function<T> newFunction(Function<T> parent, int paramIndex, Function<U> arg)
			throws TypeMismatchException;
	
	public <T,U> Function<T> newFunction(Function<T> parent, int paramIndex, U arg)
			throws TypeMismatchException;
	
}
