package jprobe.framework.model.types;

public interface Typed<T extends Typed<? extends T>> {
	
	public Type<? extends T> getType();
	
}
