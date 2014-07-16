package jprobe.framework.model.types;

import java.util.Deque;
import java.util.LinkedList;

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
	public static <T extends Typed<T>> Type<T> typeOf(T o){
		if(o == null){
			return null;
		}
		return o.getType();
	}
	
	public static <T> ObjectType<T> typeOf(T o){
		if(o == null) return null;
		return new ObjectType<T>((Class<? extends T>) o.getClass());
	}
	
	public static <T> ObjectType<T> asObjectType(Class<T> clazz){
		return new ObjectType<T>(clazz);
	}
	
	public static <T> Deque<Type<? extends T>> typesOf(Deque<? extends T> objs){
		Deque<Type<? extends T>> types = new LinkedList<Type<? extends T>>();
		for(T obj : objs){
			types.add(Types.typeOf(obj));
		}
		return types;
	}
	
	public static Type<?>[] typesOf(Object ... objs){
		Type<?>[] types = new Type<?>[objs.length];
		for(int i=0; i<objs.length; ++i){
			types[i] = Types.typeOf(objs[i]);
		}
		return types;
	}
	
	public static boolean isInstance(Type<?>[] types, Object[] objs){
		if(types.length == objs.length){
			for(int i=0; i<types.length; ++i){
				if(!types[i].isInstance(objs[i])){
					return false;
				}
			}
			return true;
		}
		return false;
	}
	
	public static boolean isAssignableFrom(Type<?>[] types, Type<?>[] from){
		if(types.length == from.length){
			for(int i=0; i<types.length; ++i){
				if(!types[i].isAssignableFrom(from[i])){
					return false;
				}
			}
			return true;
		}
		return false;
	}
	
	
}
