package jprobe.system.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import util.tuple.Tuple2;
import util.tuple.Tuple3;
import jprobe.framework.model.Function;
import jprobe.framework.model.InvocationException;
import jprobe.framework.model.Parameter;
import jprobe.framework.model.TypeMismatchException;

public class ChildFunction<R> extends Function<R> {
	private static final long serialVersionUID = 1L;
	
	private final FunctionFactory m_Factory;
	
	private final Function<R> m_Parent;
	private final List<Tuple3<Integer, Function<?>, List<Integer>>> m_Functions;
	
	private final Parameter<?>[] m_Params;
	
	public ChildFunction(FunctionFactory factory, Function<R> parent, List<Tuple2<Integer, Function<?>>> functions)
			throws IllegalArgumentException, TypeMismatchException{
		m_Factory = factory;
		m_Parent = parent;
		List<Tuple2<Integer, Function<?>>> sorted = new ArrayList<Tuple2<Integer, Function<?>>>(functions);
		Collections.sort(sorted, new Comparator<Tuple2<Integer, Function<?>>>(){

			@Override
			public int compare(Tuple2<Integer, Function<?>> arg0,
					Tuple2<Integer, Function<?>> arg1) {
				return arg0.first - arg1.first;
			}
			
		});
		
		m_Functions = new ArrayList<Tuple3<Integer, Function<?>, List<Integer>>>(sorted.size());
		List<Parameter<?>> params = new ArrayList<Parameter<?>>();
		for(Parameter<?> param : parent.getParameters()){
			params.add(param);
		}
		
		int offset = 0;
		for(int i=0; i<sorted.size(); ++i){
			Tuple2<Integer, Function<?>> tuple2 = sorted.get(i);
			int index = tuple2.first + offset;
			Function<?> arg = tuple2.second;
			Parameter<?> defined = params.get(index);
			params.remove(index);
			List<Integer> argParamIndices;
			if(arg != null){
				argParamIndices = Parameters.assignFunctionToParameter(defined, arg);
				Parameter<?>[] argParams = arg.getParameters();
				List<Parameter<?>> undefinedParams = new ArrayList<Parameter<?>>(argParamIndices.size());
				for(int argIndex : argParamIndices){
					undefinedParams.add(argParams[argIndex]);
				}
				params.addAll(index, undefinedParams);
			}else{
				argParamIndices = new ArrayList<Integer>();
			}
			offset += argParamIndices.size() - 1;
			Tuple3<Integer, Function<?>, List<Integer>> tuple3 = 
					new Tuple3<Integer, Function<?>, List<Integer>>(index, arg, argParamIndices);
			m_Functions.add(tuple3);
		}
		
		m_Params = params.toArray(new Parameter<?>[params.size()]);
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
	public Function<R> putArguments(int[] indices, Function<?>[] args)
			throws TypeMismatchException {
		return m_Factory.newFunction(this, indices, args);
	}

	@Override
	public Function<R> putArguments(int[] indices, Object[] args)
			throws TypeMismatchException {
		return m_Factory.newFunction(this, indices, args);
	}
	
	private Function<?>[] getAndRemoveFunctionArgs(int function, List<Function<?>> args){
		int offset = m_Functions.get(function).first;
		int length = m_Functions.get(function).third.size();
		
		Function<?>[] functionArgs = new Function<?>[length];
		for(int i=0; i<length; i++){
			int argsIndex = i + offset;
			functionArgs[i] = args.get(argsIndex);
			args.remove(argsIndex);
			--offset;
		}

		return functionArgs;
	}
	
	private Function<?> applyArguments(int function, Function<?>[] args) throws TypeMismatchException{
		int[] indices = toArray(m_Functions.get(function).third);
		return m_Functions.get(function).second.putArguments(indices, args);
	}
	
	private Function<?>[] getFunctionsArray(){
		Function<?>[] array = new Function<?>[m_Functions.size()];
		for(int i=0; i<m_Functions.size(); ++i){
			array[i] = m_Functions.get(i).second;
		}
		return array;
	}
	
	private static int[] toArray(List<Integer> list){
		int[] array = new int[list.size()];
		for(int i=0; i<list.size(); i++){
			array[i] = list.get(i);
		}
		return array;
	}

	@Override
	public R invoke(Function<?>... args) throws IllegalArgumentException,
			TypeMismatchException, InvocationException {
		Parameter<?>[] params = this.getParameters();
		//check that params and args match
		Parameters.checkArguments(this, params, args);
		
		if(args == null || args.length == 0){
			return m_Parent.invoke(this.getFunctionsArray());
		}
		
		List<Function<?>> values = new ArrayList<Function<?>>();
		for(int i=0; i<args.length; i++){
			values.add(args[i]);
		}
				//Arrays.asList(args);
		for(int i=0; i<m_Functions.size(); i++){
			Tuple3<Integer, Function<?>, List<Integer>> tuple = m_Functions.get(i);
			Function<?>[] funcArgs = this.getAndRemoveFunctionArgs(i, values);
			Function<?> func = this.applyArguments(i, funcArgs);
			values.add(tuple.first, func);
		}

		//invoke parent function with parentArgs
		return m_Parent.invoke(values.toArray(new Function<?>[values.size()]));
		
	}





}
