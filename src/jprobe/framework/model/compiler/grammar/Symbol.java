package jprobe.framework.model.compiler.grammar;

import java.io.Serializable;

public abstract class Symbol<V> implements Serializable{
	private static final long serialVersionUID = 1L;
	
	public abstract void accept(V visitor);
	
	public abstract Class<? extends Symbol<V>> getSymbolType();
	
}
