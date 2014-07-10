package jprobe.system.model;

import jprobe.framework.model.Function;
import jprobe.framework.model.Parameter;
import jprobe.framework.model.TypeMismatchException;
import jprobe.framework.model.Value;

public class Parameters {
	
	public static void checkType(Parameter<?> param, Function<?> func) throws TypeMismatchException{
		if(!param.getType().isAssignableFrom(func.returnType())){
			throw new TypeMismatchException("Parameter "+param+" type "+param.getType()+
					" is not assignable from function "+func+" return type "+func.returnType());
		}
	}
	
	public static void checkType(Parameter<?> param, Value<?> value) throws TypeMismatchException{
		if(!param.getType().isAssignableFrom(value.getType())){
			throw new TypeMismatchException("Parameter "+param+" type "+param.getType()+
					" is not assignable from value "+value+" type "+value.getType());
		}
	}
	
	public static <T> void checkType(Parameter<?> param, T value) throws TypeMismatchException{
		if(!param.getType().isAssignableFrom(value.getClass())){
			throw new TypeMismatchException("Parameter "+param+" type "+param.getType()+
					" is not assignable from object "+value+" of class "+value.getClass());
		}
	}
	
	public static void checkArguments(Function<?> f, Parameter<?>[] params, Value<?> ... args)
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
