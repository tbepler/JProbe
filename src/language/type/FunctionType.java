package language.type;

import java.util.Collection;

public class FunctionType implements Type{
	
	public FunctionType(Type param, Type ret){
		//TODO
	}

	@Override
	public boolean isAssignableFrom(Type t) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSuperTypeOf(Type t) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Collection<Type> getSuperTypes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Type apply(Type arg) throws IllegalApplicationException {
		// TODO Auto-generated method stub
		return null;
	}

}
