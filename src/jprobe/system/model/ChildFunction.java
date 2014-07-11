package jprobe.system.model;

import java.util.List;

import jprobe.framework.model.Function;
import jprobe.framework.model.InvocationException;
import jprobe.framework.model.Parameter;
import jprobe.framework.model.TypeMismatchException;

public class ChildFunction<R> extends Function<R> {
	private static final long serialVersionUID = 1L;
	
	private final FunctionFactory m_Factory;
	
	private final Function<R> m_Parent;
	
	private final int m_ParamIndex;
	private final int m_ParentArgsLength;
	
	private final Function<?> m_ValueFunction;
	private final int[] m_ValueParamIndices;
	
	private final Parameter<?>[] m_Params;
	private final int m_ValueFuncParamsStart;
	private final int m_ValueFuncParamsLength;
	
	public ChildFunction(FunctionFactory factory, Function<R> parent, int paramIndex, Function<?> valueFunction)
			throws IllegalArgumentException, TypeMismatchException{
		m_Factory = factory;
		m_Parent = parent;
		m_ParamIndex = paramIndex;
		m_ValueFunction = valueFunction;
		
		Parameter<?>[] parentParams = parent.getParameters();
		m_ParentArgsLength = parentParams.length;
		
		Parameter<?>[] undefinedValueParams;
		
		if(valueFunction != null){
			Parameter<?>[] valueParams = valueFunction.getParameters();
			Parameter<?> defined = parentParams[paramIndex];

			List<Integer> undefinedValueParamIndices = Parameters.assignFunctionToParameter(defined, valueFunction);
			m_ValueParamIndices = new int[undefinedValueParamIndices.size()];
			for(int i=0; i<m_ValueParamIndices.length; ++i){
				m_ValueParamIndices[i] = undefinedValueParamIndices.get(i);
			}
			undefinedValueParams = new Parameter<?>[m_ValueParamIndices.length];
			for(int i=0; i<undefinedValueParams.length; ++i){
				int valueIndex = m_ValueParamIndices[i];
				undefinedValueParams[i] = valueParams[valueIndex];
			}
		}else{
			m_ValueParamIndices = new int[]{};
			undefinedValueParams = new Parameter<?>[]{};
		}
		
		m_ValueFuncParamsStart = paramIndex;
		m_ValueFuncParamsLength = undefinedValueParams.length;
		
		m_Params = new Parameter<?>[parentParams.length + undefinedValueParams.length - 1];
		System.arraycopy(parentParams, 0, m_Params, 0, paramIndex);
		System.arraycopy(undefinedValueParams, 0, m_Params, paramIndex, undefinedValueParams.length);
		System.arraycopy(parentParams, paramIndex+1, m_Params, paramIndex + undefinedValueParams.length, parentParams.length - paramIndex - 1);
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
