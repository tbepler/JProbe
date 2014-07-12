package jprobe.framework.model.types;

public abstract class Signature<R,T extends Typed<T>> extends Type<T>{
	private static final long serialVersionUID = 1L;
	
	public Signature() {
		super(Types.SIGNATURE);
	}
	
	public abstract Signature<?,?>[] getParameters();
	
	/**
	 * Returns the {@link Type} of this signature's return value
	 * 
	 * @return - type of this signatures return value
	 */
	public abstract Type<R> getReturnType();
	
	@Override
	public String toString(){
		StringBuilder builder = new StringBuilder();
		builder.append(this.getReturnType());
		Signature<?,?>[] params = this.getParameters();
		if(params.length > 0){
			builder.append(" ");
			builder.append(signatureArrayToString(params));
		}
		return builder.toString();
	}
	
	public static String signatureArrayToString(Signature<?,?>[] array){
		StringBuilder builder = new StringBuilder();
		builder.append("(");
		boolean first = true;
		for(Signature<?,?> s : array){
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
	
	@Override
	public final boolean isAssignableFrom(Type<?> type){
		if(type == null) return false;
		if(type == this) return true;
		if(type instanceof Signature){
			Signature<?,?> other = (Signature<?,?>) type;
			if(this.getReturnType().isAssignableFrom(other.getReturnType())){
				Signature<?,?>[] oParams = other.getParameters();
				Signature<?,?>[] tParams = this.getParameters();
				return signaturesAssignableFrom(oParams, tParams);
			}
		}
		return false;
	}
	
	private static boolean signaturesAssignableFrom(Signature<?,?>[] array, Signature<?,?>[] assignableFrom){
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

	@Override
	public boolean isInstance(Object obj) {
		Type<?> type = Types.typeOf(obj);
		return this.isAssignableFrom(type);
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
