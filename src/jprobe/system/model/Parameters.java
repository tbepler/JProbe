package jprobe.system.model;

import jprobe.framework.model.Function;
import jprobe.framework.model.Parameter;
import jprobe.framework.model.TypeMismatchException;

public class Parameters {
	
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
