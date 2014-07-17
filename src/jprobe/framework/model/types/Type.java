package jprobe.framework.model.types;

import java.io.Serializable;
import jprobe.framework.model.Pointer;

public interface Type<T> extends Serializable{
	
	/**
	 * Tests if this type supports boxing and unboxing.
	 * @return True if boxing/unboxing of this type is supported.
	 */
	public boolean isBoxable();
	
	/**
	 * The size of the underlying types array boxed by
	 * this type or -1 if boxing is not supported.
	 * @return
	 */
	public int size();
	
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
	public Pointer[] unbox(T obj);
	
	/**
	 * Tests whether this type could box a deque of objects
	 * with the given types.
	 * @param types - array of types to test
	 * @return True if this type could box an array of objects
	 * with the given types
	 */
	public boolean canBox(Type<?>[] types);
	
	public Type<?> box(Type<?>[] types);
	
	/**
	 * Consumes objects from the given deque and boxes them
	 * into an object of this type.
	 * @param objs - objects to box
	 * @return - object of this type that boxes the given
	 * objects or null if boxing is not supported by this 
	 * type
	 */
	public T box(Pointer[] objs);
	
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
