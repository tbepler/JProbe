package jprobe.framework.model.function;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import util.ArrayUtils;
import util.tuple.TupleClass;

public abstract class Signature implements Serializable{
	private static final long serialVersionUID = 1L;
	
	public static final String VOID = "void";
	
	public abstract Signature[] getParameters();
	public abstract Signature[] getReturnType();
	
	@Override
	public String toString(){
		StringBuilder builder = new StringBuilder();
		builder.append(returnTypeToString(this.getReturnType()));
		Signature[] params = this.getParameters();
		if(params.length > 0){
			builder.append(" ");
			builder.append(signatureArrayToString(params));
		}
		return builder.toString();
	}
	
	public static String returnTypeToString(Signature[] type){
		if(type.length > 1){
			return signatureArrayToString(type);
		}else if(type.length == 1){
			return String.valueOf(type[0]);
		}else{
			//the empty TupleClass is the void return type
			return VOID;
		}
	}
	
	public static String signatureArrayToString(Signature[] array){
		StringBuilder builder = new StringBuilder();
		builder.append("(");
		boolean first = true;
		for(Signature s : array){
			if(first){
				builder.append(s);
				first = false;
			}else{
				builder.append(", ").append(s);
			}
		}
		builder.append(")");
		return builder.toString();
	}
	
	
	public final boolean subsignatureOf(Signature other){
		if(other == null) return false;
		if(other == this) return true;
		if(this.getReturnType().isAssignableFrom(other.getReturnType())){
			Signature[] oParams = other.getParameters();
			Signature[] tParams = this.getParameters();
			return parametersSubsetOf(tParams, oParams);
		}
		return false;
	}
	
	private static boolean parametersSubsetOf(Signature[] params, Signature[] parentSet){
		if(params == parentSet) return true;
		if((parentSet == null || parentSet.length == 0) && (params == null || params.length == 0)){
			return true;
		}
		if(parentSet == null) return false;
		if(params == null) return true;
		if(params.length <= parentSet.length){
			List<Signature> paramsRemoved = removeAssignableSignatures(parentSet, params);
			return (paramsRemoved.size() == parentSet.length - params.length);
		}
		return false;
	}
	
	public static List<Signature> removeAssignableSignatures(Signature[] set, Signature[] remove){
		List<Signature> list = ArrayUtils.toList(set);
		if(remove != null){
			for(int i=remove.length-1; i>=0; --i){
				Signature r = remove[i];
				for(int j=list.size()-1; j>=0; --j){
					if(list.get(j).isAssignableFrom(r)){
						list.remove(j);
						break;
					}
				}
			}
		}
		return list;
	}
	
	public static List<Integer> getUnassignableSignatureIndices(Signature[] set, Signature[] assign){
		List<Signature> list = ArrayUtils.toList(set);
		List<Integer> indices = new ArrayList<Integer>(list.size());
		for(int i=0; i<indices.size(); i++){
			indices.add(i);
		}
		if(assign != null){
			for(int i=assign.length-1; i>=0; --i){
				Signature r = assign[i];
				for(int j=list.size()-1; j>=0; --j){
					if(list.get(j).isAssignableFrom(r)){
						list.remove(j);
						indices.remove(j);
						break;
					}
				}
			}
		}
		return indices;
	}
	
	public final boolean isAssignableFrom(Signature other){
		if(other == null) return false;
		if(other == this) return true;
		Signature[] oReturn = other.getReturnType();
		Signature[] tReturn = this.getReturnType();
		if(signaturesAssignableFrom(tReturn, oReturn)){
			Signature[] oParams = other.getParameters();
			Signature[] tParams = this.getParameters();
			return signaturesAssignableFrom(oParams, tParams);
		}
		return false;
	}
	
	private static boolean signaturesAssignableFrom(Signature[] array, Signature[] assignableFrom){
		if(array == assignableFrom) return true;
		if((array == null || array.length == 0) && (assignableFrom == null || assignableFrom.length == 0)){
			return true;
		}
		if(array == null || assignableFrom == null) return false;
		if(array.length == assignableFrom.length){
			for(int i=0; i<array.length; i++){
				if(!array[i].isAssignableFrom(assignableFrom[i])){
					return false;
				}
			}
			return true;
		}
		return false;
	}
	
/*	
	@Override
	public final boolean equals(Object o){
		if(o == null) return false;
		if(o == this) return true;
		if(o instanceof Signature){
			Signature sig = (Signature) o;
			if(this.getReturnType().equals(sig.getReturnType())){
				Signature[] oParams = sig.getParameters();
				Signature[] tParams = this.getParameters();
				return parametersEqual(oParams, tParams);
			}
		}
		return false;
	}
	
	private static boolean parametersEqual(Signature[] params1, Signature[] params2){
		if(params1.length == params2.length){
			for(int i=0; i<params1.length; i++){
				if(!params1[i].equals(params2[i])){
					return false;
				}
			}
			return true;
		}
		return false;
	}
	
*/
	
}
