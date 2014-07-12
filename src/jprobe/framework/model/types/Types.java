package jprobe.framework.model.types;

import jprobe.framework.model.function.Procedure;
import jprobe.framework.model.tuple.Tuple;

public enum Types {
	
	CLASS,
	SIGNATURE,
	TUPLE,
	VOID;
	
	/**
	 * Returns an Object representing the type of the given object.
	 * If the given object is a {@link Tuple} then a {@link Type} containing
	 * its {@link TupleClass}
	 * is returned. If the given object is a {@link Procedure} than a Type containing
	 * its
	 * {@link Signature} is returned. If the object is null, then a 
	 * @param o
	 * @return
	 */
	public static <T> Type<T> typeOf(T o){
		if(o == null){
			return null;
		}
		if(o instanceof Tuple){
			Tuple t = (Tuple) o;
			return t.getType();
		}
		if(o instanceof Procedure){
			Procedure p = (Procedure) o;
			return p.getSignature();
		}
		return o.getClass();
	}
	
	public static boolean 
	
}
