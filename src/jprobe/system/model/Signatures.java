package jprobe.system.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import util.tuple.TupleClass;
import jprobe.framework.model.function.Signature;

public final class Signatures {
	
	private static final Signature[] EMPTY = new Signature[]{};
	
	public static List<Signature> bridgeSignatures(Signature target, Signature current){
		if(target.isAssignableFrom(current)){
			return Collections.emptyList();
		}
	}
	
	public static List<Signature> bridgeParameters(Signature[] target, Signature[] current){
		if(parametersAssignableFrom(current, target)){
			//if the current parameters can be assigned as if they were the target parameters, then
			//no bridge is required
			return Collections.emptyList();
		}
		//deal with some special cases
		if(current == null || current.length == 0){
			//the current function requires no parameters, so the bridge function would be
			//of the form:
			// (target1, target2, ... ) => ()
			//this means that no bridge is required, as this is a void function signature
			return Collections.emptyList();
		}
		if(target == null || target.length == 0){
			//the target function requires no parameters, so the bridge function would be
			//of the form:
			// () => (current1, current2, ...)
			//instead, split each tuple element into its own function of the form:
			// () => current1
			// () => curent2
			// () => ...
			return new ArrayList<Signature>(Arrays.asList(current));
		}
		//generate a signature for a function that will bridge between the target and current
		//parameter signatures.
		//this signature describes a function of the form:
		// (target1, target2, ...) => (current1, current2, ...)
		
	}
	
	private static Signature createNoParamsSignature(Class<?> returnType){
		return new DefaultSignature(returnType);
	}
	
	private static Signature createNoParamsSignature(TupleClass returnType){
		return new DefaultSignature(returnType);
	}
	
	public static boolean parametersAssignableFrom(Signature[] params, Signature[] assignableFrom){
		if(params == assignableFrom) return true;
		if((params == null || params.length == 0) && (assignableFrom == null || assignableFrom.length == 0)){
			return true;
		}
		if(params == null || assignableFrom == null) return false;
		if(params.length == assignableFrom.length){
			for(int i=0; i<params.length; ++i){
				if(!params[i].isAssignableFrom(assignableFrom[i])){
					return false;
				}
			}
			return true;
		}
		return false;
	}
	
}
