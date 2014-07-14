package jprobe.framework.model.types;

import java.io.Serializable;
import java.util.Deque;

public interface Type<T> extends Serializable{

	/**
	 * Extracts an object of this type from the given objects,
	 * if possible. This method consumes objects starting from
	 * the deque head and will consume as many elements from the deque as
	 * necessary to create this type. This method should always return
	 * if the {@link #isExtractableFrom(Type...)} method returns
	 * true for the types of the objects. If an exception is thrown,
	 * then no items should be removed from the deque.
	 * @param objs - objects from which to cast to this type
	 * @return the cast object
	 * @throws ClassCastException - if the object cannot
	 * be cast to this type
	 */
	public T extract(Deque<Object> objs);
	
	/**
	 * Checks if an object of this type can be extracted from
	 * a deque of objects of the given types. In other words, can object instances
	 * of the given types be used to produce an object of this type.
	 * This method consumes types starting from
	 * the deque head and will consume as many elements
	 * as necessary to assign this type. If false is returned, then no
	 * items should be removed from the deque.
	 * @param types - types from which to assign this type
	 * @return boolean indicating the result
	 */
	public boolean isExtractableFrom(Deque<Type<?>> types);
	
	/**
	 * Tests whether an object of this type can be extracted from
	 * the given deque of objects. When this function returns, the
	 * deque will be in the same state as it was received.
	 * @param objs - deque of objects to test whether an object
	 * of this type could be extracted from
	 * @return True if an object of this type could be extracted
	 * from the given deque, False otherwise
	 */
	public boolean canExtract(Deque<Object> objs);
	
	public T cast(Object obj);
	
	/**
	 * Checks if the given type is equal to this type
	 * or is a subtype of this type. Returns
	 * False if the given type is null.
	 * @param other - type to check
	 * @return True if the given type is equal to or a 
	 * subtype of this type. False otherwise.
	 */
	public boolean isAssignableFrom(Type<?> type);
	
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
