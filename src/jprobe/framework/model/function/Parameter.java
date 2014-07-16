package jprobe.framework.model.function;

import java.io.Serializable;

import jprobe.framework.model.types.Type;

public abstract class Parameter<R> implements Serializable{
	private static final long serialVersionUID = 1L;

	public abstract String getName();
	public abstract String getDescription();
	public abstract String getCategory();
	public abstract Character getFlag();
	public abstract String getPrototype();
	
	public abstract Type<R> getArgumentType();
	
	/*
	 * if Null - this parameter is required
	 * if not Null - this parameter is optional
	 */
	public abstract R getDefaultValue();
	
	public abstract boolean isOptional();
	
	
	
}
