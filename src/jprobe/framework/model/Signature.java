package jprobe.framework.model;

import java.io.Serializable;

public abstract class Signature<T> implements Serializable{
	private static final long serialVersionUID = 1L;

	public abstract Signature<?>[] getParameters();
	public abstract Class<? extends T> getReturnType();
	
	@Override
	public String toString(){
		StringBuilder builder = new StringBuilder();
		builder.append(this.getReturnType()).append(" (");
		boolean first = true;
		for(Signature<?> s : this.getParameters()){
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
	
	public final boolean isAssignableFrom(Signature<?> other){
		if(other == null) return false;
		if(other == this) return true;
		if(this.getReturnType().isAssignableFrom(other.getReturnType())){
			Signature<?>[] oParams = other.getParameters();
			Signature<?>[] tParams = this.getParameters();
			return parametersAssignableFrom(oParams, tParams);
		}
		return false;
	}
	
	private static boolean parametersAssignableFrom(Signature<?>[] params, Signature<?>[] assignableFrom){
		if(params.length == assignableFrom.length){
			for(int i=0; i<params.length; i++){
				if(!params[i].isAssignableFrom(assignableFrom[i])){
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
			Signature<?> sig = (Signature<?>) o;
			if(this.getReturnType().equals(sig.getReturnType())){
				Signature<?>[] oParams = sig.getParameters();
				Signature<?>[] tParams = this.getParameters();
				return parametersEqual(oParams, tParams);
			}
		}
		return false;
	}
	
	private static boolean parametersEqual(Signature<?>[] params1, Signature<?>[] params2){
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
