package jprobe.framework.model;

import java.io.Serializable;

public interface Parameter<T> extends Serializable{
	
	public String getName();
	public String getDescription();
	public String getCategory();
	public Character getFlag();
	public String getPrototype();
	
	public Class<? extends T> getType();
	
	/*
	 * if Null - this parameter is required
	 * if not Null - this parameter is optional
	 */
	public T getDefaultValue();
	
	public boolean isOptional();
	
	
}
