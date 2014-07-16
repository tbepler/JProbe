package jprobe.framework.model.types;

import java.io.Serializable;
import java.util.Deque;

public interface Type<T> extends Serializable{
	
	/**
	 * Tests if this type supports boxing and unboxing.
	 * @return True if boxing/unboxing of this type is supported.
	 */
	public boolean isBoxable();
	
	/**
	 * Returns the number of elements this type boxes, or -1
	 * if boxing is unsupported.
	 * @return
	 */
	public int boxSize();
	
	/**
	 * Unboxes this type into its enclosed types.
	 * @return Array of unboxed types or null if boxing is
	 * not supported by this type
	 */
	public Type<?>[] unbox();
	
	/**
	 * Unboxes an object of this type into its enclosed
	 * objects.
	 * @param obj - object to unbox
	 * @return objects unboxed from the given object or
	 * null if boxing is unsupported by this type
	 */
	public Object[] unbox(T obj);
	
	/**
	 * Tests whether this type could box a deque of objects
	 * with the given types.
	 * @param types - array of types to test
	 * @return True if this type could box an array of objects
	 * with the given types
	 */
	public boolean canBox(Deque<Type<?>> types);
	
	/**
	 * Consumes objects from the given deque and boxes them
	 * into an object of this type.
	 * @param objs - objects to box
	 * @return - object of this type that boxes the given
	 * objects or null if boxing is not supported by this 
	 * type
	 */
	public T box(Deque<Object> objs);

	/**
	 * Extracts an object of this type from the given objects,
	 * if possible. This method consumes objects starting from
	 * the deque head and will consume as many elements from the deque as
	 * necessary to create this type. This method should always return
	 * if the {@link #isExtractableFrom(Deque)} method returns
	 * true for the types of the objects. If an exception is thrown,
	 * then no items should be removed from the deque.
	 * @param objs - objects from which to cast to this type
	 * @return the cast object
	 * @throws ClassCastException - if the object cannot
	 * be cast to this type
	 */
	public T extract(Deque<Object> objs);
	
	public T extract(Object obj);
	
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
	
	public boolean isExtractableFrom(Type<?> type);
	
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
	
	public boolean canExtract(Object obj);
	
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
