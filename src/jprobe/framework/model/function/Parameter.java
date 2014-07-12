package jprobe.framework.model.function;

import java.io.Serializable;

import jprobe.framework.model.types.Signature;
import jprobe.framework.model.types.Typed;

public abstract class Parameter<R,T extends Function<R,T>> implements Typed<T>, Serializable{
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
	public abstract Function<? extends R,T> getDefaultValue();
	
	public abstract boolean isOptional();
	
	
	
}