package jprobe.framework.model.compiler.grammar;

import java.util.List;

public interface Production<V> {
	
	public Class<? extends Symbol<V>> leftHandSide();
	
	public List<Class<? extends Symbol<V>>> rightHandSide();
	
	public Symbol<V> reduce(Symbol<V> ... symbols);
	
}
