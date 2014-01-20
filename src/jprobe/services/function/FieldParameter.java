package jprobe.services.function;

import jprobe.services.data.Field;

public interface FieldParameter {
	
	public Field getType();
	public boolean isValid(Field field);
	
}
