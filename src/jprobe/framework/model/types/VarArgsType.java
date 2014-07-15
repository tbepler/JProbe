package jprobe.framework.model.types;

public interface VarArgsType<T> extends BoxableType<T> {
	
	public int minArgs();
	public int maxArgs();
	
}
