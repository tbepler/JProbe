package language.type;

import java.util.Collection;

public interface Type {
	
	public boolean equals(Object o);
	public boolean isAssignableFrom(Type t);
	public boolean isSuperTypeOf(Type t);
	public Collection<Type> getSuperTypes();
	
	public Type apply(Type arg) throws IllegalApplicationException;
	
}
