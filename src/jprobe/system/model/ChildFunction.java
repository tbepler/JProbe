package jprobe.system.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import jprobe.framework.model.Function;
import jprobe.framework.model.MissingArgumentsException;
import jprobe.framework.model.Parameter;
import jprobe.framework.model.Value;

public class ChildFunction<R,D> implements Function<R> {
	private static final long serialVersionUID = 1L;

	private final Function<R> m_Parent;
	
	private final Parameter<D> m_Defined;
	private final Function<D> m_ValueFunction;
	
	private final List<Parameter<?>> m_Params;
	
	public ChildFunction(Function<R> parent, Parameter<D> defined, Function<D> valueFunction){
		m_Parent = parent;
		m_Defined = defined;
		m_ValueFunction = valueFunction;
		m_Params = new ArrayList<Parameter<?>>(m_Parent.getParameters());
		for(int i=0; i<m_Params.size(); i++){
			if(m_Params.get(i) == m_Defined){
				m_Params.addAll(i, m_ValueFunction.getParameters());
				m_Params.remove(m_Defined);
				break;
			}
		}
	}

	@Override
	public List<Parameter<?>> getParameters() {
		return Collections.unmodifiableList(m_Params);
	}

	@Override
	public Class<R> returnType() {
		return m_Parent.returnType();
	}

	@Override
	public <T> Function<R> putArgument(Parameter<T> param, Function<T> arg) {
		return new ChildFunction<R,T>(this, param, arg);
	}

	@Override
	public R call() throws MissingArgumentsException, ExecutionException {
		return this.call(new HashMap<Parameter<?>, Value<?>>());
	}

	@Override
	public R call(Map<Parameter<?>, Value<?>> args) throws MissingArgumentsException, ExecutionException {
		this.process(args);
		System.err.println(args.size());
		return m_Parent.call(args);
	}
	
	public void process(Map<Parameter<?>, Value<?>> args){
		Map<Parameter<?>, Value<?>> valueFunctionArgs = new HashMap<Parameter<?>, Value<?>>();
		for(Parameter<?> param : m_ValueFunction.getParameters()){
			if(args.containsKey(param)){
				valueFunctionArgs.put(param, args.get(param));
				args.remove(param);
			}
		}
		System.err.println("["+m_Parent+"] " +this + " defines: "+m_Defined + " valueFunction: "+m_ValueFunction);
		System.err.println("Value args: "+valueFunctionArgs.size());
		
		args.put(m_Defined, new FunctionValue<D>(m_ValueFunction, valueFunctionArgs));
		System.err.println("Parent args: "+args.size());
	}

}
