package jprobe.system.model;

import jprobe.framework.model.Function;
import jprobe.framework.model.InvocationException;
import jprobe.framework.model.Parameter;
import jprobe.framework.model.Procedure;
import jprobe.framework.model.TypeMismatchException;

public class RootFunction<R> extends Function<R> {
	private static final long serialVersionUID = 1L;
	
	private final FunctionFactory m_Factory;
	private final Procedure<R> m_Procedure;
	
	public RootFunction(FunctionFactory factory, Procedure<R> procedure){
		m_Factory = factory;
		m_Procedure = procedure;
	}
	
	@Override
	public Parameter<?>[] getParameters() {
		return m_Procedure.getParameters();
	}

	@Override
	public Class<? extends R> getReturnType() {
		return m_Procedure.getReturnType();
	}

	@Override
	public <T> Function<R> putArgument(int paramIndex, Function<T> arg) 
			throws TypeMismatchException{
		return m_Factory.newFunction(this, paramIndex, arg);
	}

	@Override
	public <T> Function<R> putArgument(int paramIndex, T arg)
			throws TypeMismatchException {
		return m_Factory.newFunction(this, paramIndex, arg);
	}

	@Override
	public R invoke(Function<?>... args)
			throws IllegalArgumentException, TypeMismatchException, InvocationException {
		
		Parameter<?>[] params = this.getParameters();
		//first check that the args and params match
		Parameters.checkArguments(this, params, args);
		
		//invoke the procedure
		return m_Procedure.invoke(args);
	}



}
