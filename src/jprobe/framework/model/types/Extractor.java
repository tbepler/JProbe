package jprobe.framework.model.types;

import java.io.Serializable;
import java.util.Deque;

public abstract class Extractor<E, T extends Type<E>> implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private final Class<T> m_ExtractType;
	
	protected Extractor(Class<T> typeClass){
		m_ExtractType = typeClass;
	}
	
	public Class<T> getExtractionTypeClass(){
		return m_ExtractType;
	}

	/**
	 * Extracts an object of the given type from the given objects,
	 * if possible. This method consumes objects starting from
	 * the deque head and will consume as many elements from the deque as
	 * necessary to create an object of the type. This method should always return
	 * if the {@link } method returns
	 * true for the types of the objects. If an exception is thrown,
	 * then no items should be removed from the deque.
	 * @param type - the type of the object which should be extracted
	 * @param objs - objects from which to cast to this type
	 * @return the cast object
	 * @throws ClassCastException - if the object cannot
	 * be cast to this type
	 */
	public abstract E extract(T type, Deque<Object> objs);
	
	/**
	 * Checks if an object of the specified type can be extracted from
	 * a deque of objects of the given types. In other words, can object instances
	 * of the given types be used to produce an object of the specified type.
	 * This method consumes types starting from
	 * the deque head and will consume as many elements
	 * as necessary to assign the type. If false is returned, then no
	 * items should be removed from the deque.
	 * @param type - the type that should be tested for extraction
	 * @param extractFrom - types from which to assign this type
	 * @return boolean indicating the result
	 */
	public abstract boolean isExtractableFrom(T type, Deque<Type<?>> extractFrom);
	
	/**
	 * Tests whether an object of the given type can be extracted from
	 * the specified deque of objects. When this function returns, the
	 * deque will be in the same state as it was received.
	 * @param type - the type that should be tested for extraction
	 * @param objs - deque of objects to test whether an object
	 * of this type could be extracted from
	 * @return True if an object of this extraction type could be extracted
	 * from the given deque, False otherwise
	 */
	public abstract boolean canExtract(T type, Deque<Object> objs);

}
