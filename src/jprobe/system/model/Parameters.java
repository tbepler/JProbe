package jprobe.system.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import jprobe.framework.model.Function;
import jprobe.framework.model.Parameter;
import jprobe.framework.model.Signature;
import jprobe.framework.model.TypeMismatchException;

public class Parameters {
	
	/**
	 * Returns a list of integers indicating the indices of function parameters that still need to be defined
	 * @param param
	 * @param arg
	 * @return
	 * @throws TypeMismatchException
	 */
	public static List<Integer> assignFunctionToParameter(Parameter<?> param, Function<?> arg)
			throws TypeMismatchException, IllegalArgumentException{
		//first check if arg is null, because null matches any parameter
		if(arg != null){
			//check return types
			checkReturnType(param, arg);
			//for each signature in the param's signature, make sure that the argument function has a compatible parameter 
			List<Signature<?>> paramSigs = Arrays.asList(param.getParameters());
			List<Parameter<?>> funcParams = Arrays.asList(arg.getParameters());
			
			List<Integer> remainingFuncParams = removeAllSignaturesFromParameters(paramSigs, funcParams);
			if(!paramSigs.isEmpty()){
				throw new IllegalArgumentException("Parameter "+param+
						" is not assignable from function "+arg);
			}
			return remainingFuncParams;
		}
		return Collections.emptyList();
		
	}
	
	/*
	 * Returns a list of integers indicating the indices of parameters that were not removed
	 */
	private static List<Integer> removeAllSignaturesFromParameters(List<Signature<?>> sigs, List<Parameter<?>> params){
		List<Integer> paramIndices = new ArrayList<Integer>(params.size());
		for(int i=0; i<paramIndices.size(); i++){
			paramIndices.add(i);
		}
		
		Iterator<Signature<?>> iter = sigs.iterator();
		while(iter.hasNext()){
			Signature<?> cur = iter.next();
			//remove last function parameter matching this signature
			for(int i = params.size()-1; i >= 0; --i){
				Parameter<?> funcParam = params.get(i);
				if(cur.isAssignableFrom(funcParam)){
					params.remove(i);
					paramIndices.remove(i);
					iter.remove();
					break;
				}
			}
		}
		return paramIndices;
	}
	
	public static void checkReturnType(Parameter<?> param, Function<?> func) throws TypeMismatchException{
		if(func != null){
			if(!param.getReturnType().isAssignableFrom(func.getReturnType())){
				throw new TypeMismatchException("Parameter "+param+
						" is not assignable from function "+func);
			}
		}
	}
	
	public static void checkType(Parameter<?> param, Function<?> func) throws TypeMismatchException{
		if(func == null) return;
		if(!param.isAssignableFrom(func)){
			throw new TypeMismatchException("Parameter "+param+
					" is not assignable from function "+func);
		}
	}
	
	public static <T> void checkType(Parameter<?> param, T value) throws TypeMismatchException{
		if(value == null) return;
		if(!param.getReturnType().isAssignableFrom(value.getClass()) || param.getParameters().length != 0){
			throw new TypeMismatchException("Parameter "+param+
					" is not assignable from class "+value.getClass());
		}
	}
	
	public static void checkArguments(Function<?> f, Parameter<?>[] params, Function<?> ... args)
			throws IllegalArgumentException, TypeMismatchException{
		
		if(args == null){
			if(params.length > 0){
				throw new IllegalArgumentException(f+": requires "+params.length+" args, but received 0 args.");
			}else{
				return;
			}
		}
		//check that args and params are the same length
		if(args.length != params.length){
			throw new IllegalArgumentException(f+": requires "+params.length+" args, but received "+args.length+" args.");
		}
		
		//check that the arg types match the parameter types
		for(int i=0; i<params.length; i++){
			checkType(params[i], args[i]);
		}
		
	}
	
}
