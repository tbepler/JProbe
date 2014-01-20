package jprobe.services.function;

import jprobe.services.data.Field;

public interface FieldParameter extends Parameter{
	
	public Field getType();
	public boolean isValid(Field field);
	
}
