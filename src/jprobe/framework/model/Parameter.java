package jprobe.framework.model;

public interface Parameter<T> {
	
	public String getName();
	public String getDescription();
	public String getCategory();
	public Character getFlag();
	public String getPrototype();
	
	public Class<T> getType();
	
	/*
	 * if Null - this parameter is required
	 * if not Null - this parameter is optional
	 */
	public T getDefaultValue();
	
	public boolean isOptional();
	
	
}
