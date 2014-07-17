package jprobe.framework.model;

import java.io.Serializable;

import jprobe.framework.model.types.Type;

public interface Pointer extends Serializable {

	public <T> T get();
	public Type<?> getReferenceType();
	
	
}
