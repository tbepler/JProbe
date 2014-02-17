package util.xmlserializer;

import java.lang.reflect.*;

import sun.reflect.*;

/**
 * This class is used by the XMLSerializer to instantiate objects using the serialization constructor.
 * 
 * @author Tristan Bepler
 *
 */

public class SilentObjectCreator {
	
	
	public static <T> T create(Class<T> clazz) throws ObjectInstantiationException{
		return create(clazz, Object.class);
	}
	
	public static <T> T create(Class<T> clazz, Class<? super T> parent) throws ObjectInstantiationException{
		try{
			ReflectionFactory rf = ReflectionFactory.getReflectionFactory();
			Constructor parentCons = parent.getDeclaredConstructor();
			parentCons.setAccessible(true);
			Constructor initCons = rf.newConstructorForSerialization(clazz, parentCons);
			initCons.setAccessible(true);
			return clazz.cast(initCons.newInstance());
		} catch (Exception e){
			throw new ObjectInstantiationException(e);
		}
	}
	
	
}
