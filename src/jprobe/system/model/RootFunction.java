package jprobe.system.model;

import jprobe.framework.model.Function;
import jprobe.framework.model.InvocationException;
import jprobe.framework.model.Parameter;
import jprobe.framework.model.Procedure;
import jprobe.framework.model.TypeMismatchException;
import jprobe.framework.model.Value;

public class RootFunction<R> implements Function<R> {
	private static final long serialVersionUID = 1L;
	
	private final Procedure<R> m_Procedure;
	
	public RootFunction(Procedure<R> procedure){
		m_Procedure = procedure;
	}
	
	@Override
	public Parameter<?>[] getParameters() {
		return m_Procedure.getParameters();
	}

	@Override
	public Class<? extends R> returnType() {
		return m_Procedure.returnType();
	}

	@Override
	public <T> Function<R> putArgument(int paramIndex, Function<T> arg) 
			throws TypeMismatchException{
		Parameters.checkType(this.getParameters()[paramIndex], arg);
		return new ChildFunction<R>(this, paramIndex, arg);
	}
	

	@Override
	public <T> Function<R> putArgument(int paramIndex, Value<T> arg)
			throws TypeMismatchException {
		Parameters.checkType(this.getParameters()[paramIndex], arg);
		return new ChildFunction<R>(this, paramIndex, new FixedValueFunction<T>(arg));
	}

	@Override
	public <T> Function<R> putArgument(int paramIndex, T arg)
			throws TypeMismatchException {
		Parameters.checkType(this.getParameters()[paramIndex], arg);
		return new ChildFunction<R>(this, paramIndex, new FixedValueFunction<T>(arg));
	}

	@Override
	public R invoke(Value<?>... args)
			throws IllegalArgumentException, TypeMismatchException, InvocationException {
		
		Parameter<?>[] params = this.getParameters();
		//first check that the args and params match
		Parameters.checkArguments(this, params, args);
		
		//invoke the procedure
		return m_Procedure.invoke(args);
	}



}
