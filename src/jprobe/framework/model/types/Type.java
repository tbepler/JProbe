package jprobe.framework.model.types;

import java.io.Serializable;
import java.util.Deque;

public interface Type<T> extends Serializable{
	
	/**
	 * Casts the given objects into an object of this 
	 * type, if possible. This method consumes objects starting from
	 * the deque head and will consume as many elements from the deque as
	 * necessary to cast this type. This method should always return
	 * if the {@link #isAssignableFrom(Type...)} method returns
	 * true for the types of the objects. If an exception is thrown,
	 * then no items should be removed from the deque.
	 * @param objs - objects from which to cast to this type
	 * @return the cast object
	 * @throws ClassCastException - if the object cannot
	 * be cast to this type
	 */
	public T cast(Deque<Object> objs);
	
	public T cast(Object obj);
	
	/**
	 * Checks if the given types can be assigned to
	 * this type. In other words, can object instances
	 * of the given types be cast to this type.
	 * This method consumes types starting from
	 * the deque head and will consume as many elements
	 * as necessary to assign this type. This
	 * can return True even if {@link #isInstance(Object)}
	 * would not return true, because of type wrapping/unwrapping
	 * and boxing/unboxing. If false is returned, then no
	 * items should be removed from the deque.
	 * @param types - types from which to assign this type
	 * @return boolean indicating the result
	 */
	public boolean isAssignableFrom(Deque<Type<?>> types);
	
	public boolean isAssignableFrom(Type<?> type);
	
	/**
	 * Checks if the given type is equal to this type
	 * or is a subtype of this type. This is a subset
	 * of the {@link #isAssignableFrom(Type)} types and
	 * whenever the {@link #isAssignableFrom(Type)} method
	 * returns false, this should also return false. Returns
	 * False if the given type is null.
	 * @param other - type to check
	 * @return True if the given type is equal to or a 
	 * subtype of this type. False otherwise.
	 */
	public boolean isTypeInstance(Type<?> other);
	
	/**
	 * Checks if the given object is an instance of
	 * this type or one of this type's subtypes. This
	 * is equivalent to calling {@link #isTypeInstance(Type)}
	 * with the type of the object. Returns false
	 * if the object is null.
	 * @param obj - object to check
	 * @return True if the object is an instance of
	 * this type or a subtype of this type. False
	 * otherwise.
	 */
	public boolean isInstance(Object obj);
	
}
