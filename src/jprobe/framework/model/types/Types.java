package jprobe.framework.model.types;

public enum Types {
	
	CLASS,
	SIGNATURE,
	TUPLE,
	VOID;
	
	/**
	 * Returns a {@link Type} object representing the type of the given
	 * object.
	 * @param o
	 * @return
	 */
	public static <T> Type<? extends T> typeOf(T o){
		if(o == null){
			return null;
		}
		if(o instanceof Typed){
			@SuppressWarnings("unchecked")
			Typed<? extends T> t = (Typed<? extends T>) o;
			return t.getType();
		}
		@SuppressWarnings("unchecked")
		Class<? extends T> clazz = (Class<? extends T>) o.getClass();
		return new ObjectType<T>(clazz);
	}
	
	public static <T> Type<? extends T> getType(Class<? extends T> clazz){
		return new ObjectType<T>(clazz);
	}
	
	
}
