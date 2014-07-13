package jprobe.framework.model.types;

import java.io.Serializable;

import jprobe.framework.model.tuple.Tuple2;

public interface Type<T> extends Serializable{
	
	/**
	 * Casts the given objects into an object of this 
	 * type, if possible. This method consumes objects starting from
	 * array index 0 and will consume as many array elements as
	 * necessary to cast this type. This method should always return
	 * if the {@link #isAssignableFrom(Type...)} method returns
	 * true for the types of the objects.
	 * @param objs - object array from which to cast to this type
	 * @return a tuple containing the cast object and the remaining
	 * objects from the array
	 * @throws ClassCastException - if the object cannot
	 * be cast to this type
	 */
	public Tuple2<T,Object[]> cast(Object ... objs);
	
	/**
	 * Checks if the given types can be assigned to
	 * this type. In other words, can an array of objects
	 * of the given type be cast to this type.
	 * This method consumes types starting from
	 * array index 0 and will consume as many array elements
	 * as necessary to assign this type. This
	 * can return True even if {@link #isInstance(Object)}
	 * would not return true for an array of instances of the
	 * given types, because of type wrapping/unwrapping
	 * and boxing/unboxing.
	 * @param types - type array from which to assign this type
	 * @return a tuple containing a boolean indicating the result
	 * and a Type array containing the remaining types from the array
	 */
	public Tuple2<Boolean,Type<?>[]> isAssignableFrom(Type<?> ... types);
	
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
