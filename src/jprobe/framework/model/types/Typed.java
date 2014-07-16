package jprobe.framework.model.types;

public interface Typed<T extends Typed<T>> {
	
	public Type<T> getType();
	
}
