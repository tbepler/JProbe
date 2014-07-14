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
	
	public static <T> Deque<Type<? extends T>> typesOf(Deque<? extends T> objs){
		Deque<Type<? extends T>> types = new LinkedList<Type<? extends T>>();
		for(T obj : objs){
			types.add(Types.typeOf(obj));
		}
		return types;
	}
	
	public static Object[] extract(Type<?>[] types, Deque<Object> objs){
		Deque<Object> copy = new LinkedList<Object>(objs);
		Object[] extracted = new Object[types.length];
		for(int i=0; i<types.length; ++i){
			extracted[i] = types[i].extract(objs);
		}
		int removed = objs.size() - copy.size();
		while(--removed >= 0){
			objs.poll();
		}
		return extracted;
	}
	
	public static boolean canExtract(Type<?>[] types, Deque<Object> objs){
		Deque<Type<?>> from = Types.typesOf(objs);
		return Types.isExctractableFrom(types, from);
	}
	
	public static boolean isExctractableFrom(Type<?>[] types, Deque<Type<?>> from){
		Deque<Type<?>> copy = new LinkedList<Type<?>>(from);
		for(int i=0; i<types.length; ++i){
			if(!types[i].isExtractableFrom(copy)){
				return false;
			}
		}
		int removed = from.size() - copy.size();
		while(--removed >= 0){
			from.poll();
		}
		return true;
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
