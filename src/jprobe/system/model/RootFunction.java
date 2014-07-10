package jprobe.system.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import jprobe.framework.model.Function;
import jprobe.framework.model.InvocationException;
import jprobe.framework.model.MissingArgumentsException;
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
		return new ChildFunction<R,T>(this, param, arg);
	}
	

	@Override
	public <T> Function<R> putArgument(int paramIndex, Value<T> arg)
			throws TypeMismatchException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public R invoke(Value<?>... args)
			throws MissingArgumentsException, TypeMismatchException, InvocationException {
		// TODO Auto-generated method stub
		
		
		
		args = this.processArgs(args);
		try{
			return m_Procedure.call(args);
		}catch(MissingArgumentsException e){
			throw e;
		}catch(Exception e){
			throw new ExecutionException(e);
		}
		
		return null;
	}


	private Map<Parameter<?>, Value<?>> processArgs(Map<Parameter<?>, Value<?>> args) throws MissingArgumentsException{
		Collection<Parameter<?>> missing = new ArrayList<Parameter<?>>();
		for(Parameter<?> param : this.getParameters()){
			this.processParam(param, args, missing);
		}
		if(!missing.isEmpty()){
			throw new MissingArgumentsException(this, missing);
		}
		return args;
	}
	
	private <T> void processParam(Parameter<T> param, Map<Parameter<?>, Value<?>> args, Collection<Parameter<?>> missing){
		if(!args.containsKey(param)){
			if(param.isOptional()){
				args.put(param, new DefaultValue<T>(param));
			}else{
				missing.add(param);
			}
		}
	}


}
