package jprobe.system.model;

import jprobe.framework.model.Function;
import jprobe.framework.model.InvocationException;
import jprobe.framework.model.Parameter;
import jprobe.framework.model.TypeMismatchException;
import jprobe.framework.model.Value;

public class ChildFunction<R> extends Function<R> {
	private static final long serialVersionUID = 1L;
	
	private final FunctionFactory m_Factory;
	
	private final Function<R> m_Parent;
	
	private final int m_ParamIndex;
	private final int m_ParentArgsLength;
	
	private final Function<?> m_ValueFunction;
	
	private final Parameter<?>[] m_Params;
	private final int m_ValueFuncParamsStart;
	private final int m_ValueFuncParamsLength;
	
	public ChildFunction(FunctionFactory factory, Function<R> parent, int paramIndex, Function<?> valueFunction){
		m_Factory = factory;
		m_Parent = parent;
		m_ParamIndex = paramIndex;
		m_ValueFunction = valueFunction;
		
		Parameter<?>[] parentParams = parent.getParameters();
		m_ParentArgsLength = parentParams.length;
		Parameter<?>[] valueParams = valueFunction.getParameters();
		
		Parameter<?> defined = parentParams[paramIndex];
		
		
		
		m_ValueFuncParamsStart = paramIndex;
		m_ValueFuncParamsLength = valueParams.length;
		
		m_Params = new Parameter<?>[parentParams.length + valueParams.length - 1];
		System.arraycopy(parentParams, 0, m_Params, 0, paramIndex);
		System.arraycopy(valueParams, 0, m_Params, paramIndex, valueParams.length);
		System.arraycopy(parentParams, paramIndex+1, m_Params, paramIndex + valueParams.length, parentParams.length - paramIndex - 1);
	}

	@Override
	public Parameter<?>[] getParameters() {
		return m_Params;
	}

	@Override
	public Class<? extends R> getReturnType() {
		return m_Parent.getReturnType();
	}

	@Override
	public <T> Function<R> putArgument(int paramIndex, Function<T> arg)
			throws TypeMismatchException {
		return m_Factory.newFunction(this, paramIndex, arg);
	}
	
	@Override
	public <T> Function<R> putArgument(int paramIndex, T arg)
			throws TypeMismatchException {
		return m_Factory.newFunction(this, paramIndex, arg);
	}

	@Override
	public R invoke(Function<?>... args) throws IllegalArgumentException,
			TypeMismatchException, InvocationException {
		Parameter<?>[] params = this.getParameters();
		//check that params and args match
		Parameters.checkArguments(this, params, args);
		
		Value<?> val;
		if(args == null){
			val = this.createValue(m_ValueFunction, args);
			return m_Parent.invoke(new Value<?>[]{val});
		}else{
			//allocate value function args and create Value object for invoking the value function
			Value<?>[] valueFuncArgs = new Value<?>[m_ValueFuncParamsLength];
			System.arraycopy(args, m_ValueFuncParamsStart, valueFuncArgs, 0, m_ValueFuncParamsLength);
			val = this.createValue(m_ValueFunction, valueFuncArgs);
		}
		
		//allocate the parent function args
		Value<?>[] parentArgs = new Value<?>[m_ParentArgsLength];
		System.arraycopy(args, 0, parentArgs, 0, m_ParamIndex);
		parentArgs[m_ParamIndex] = val;
		System.arraycopy(args, m_ValueFuncParamsStart + m_ValueFuncParamsLength, parentArgs, m_ParamIndex+1, m_ParentArgsLength - m_ParamIndex - 1);
		
		//invoke parent function with parentArgs
		return m_Parent.invoke(parentArgs);
		
	}
	
	protected <T> Value<T> createValue(Function<T> valueFunction, Value<?> ... args){
		return new FunctionValue<T>(valueFunction, args);
	}



}
