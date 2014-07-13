package jprobe.framework.model.types;

import java.io.Serializable;

public interface Type<T> extends Serializable{
	
	/**
	 * Casts the given object into an object of this 
	 * type, if the given object's type can be assigned
	 * to this type. This method should always return
	 * if the {@link #isAssignableFrom(Type)} method returns
	 * true for the type of the object.
	 * @param obj - object to cast to this type
	 * @return object cast into this type
	 * @throws ClassCastException - if the object cannot
	 * be cast to this type
	 */
	public T cast(Object obj);
	
	/**
	 * Checks if the given type can be assigned to
	 * this type. In other words, can an object
	 * of the given type be cast to this type. This
	 * can return True even if {@link #isInstance(Object)}
	 * would not return true for an instance of the
	 * given type, because of type wrapping/unwrapping
	 * and boxing/unboxing.
	 * @param other
	 * @return
	 */
	public boolean isAssignableFrom(Type<?> other);
	
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
