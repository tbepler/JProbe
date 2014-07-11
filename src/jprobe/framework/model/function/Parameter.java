package jprobe.framework.model.function;

import java.io.Serializable;

public abstract class Parameter<T> extends Signature<T> implements Serializable{
	private static final long serialVersionUID = 1L;

	public abstract String getName();
	public abstract String getDescription();
	public abstract String getCategory();
	public abstract Character getFlag();
	public abstract String getPrototype();

	
	/*
	 * if Null - this parameter is required
	 * if not Null - this parameter is optional
	 */
	public abstract Function<? extends T> getDefaultValue();
	
	public abstract boolean isOptional();
	
	
	
}
