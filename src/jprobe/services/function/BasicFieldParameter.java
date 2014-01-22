package jprobe.services.function;

import jprobe.services.data.Field;

public class BasicFieldParameter extends AbstractFieldParameter{

	public BasicFieldParameter(String name, String description, boolean optional, Field type) {
		super(name, description, optional, type);
	}

	@Override
	public boolean isValid(Field field) {
		return this.getType().getClass() == field.getClass();
	}

}
