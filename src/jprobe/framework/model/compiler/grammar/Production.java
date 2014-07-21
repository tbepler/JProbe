package jprobe.framework.model.compiler.grammar;

import java.util.List;

public interface Production<V,S extends Symbol<V>> {
	
	public Class<S> leftHandSide();
	
	public List<Class<? extends Symbol<V>>> rightHandSide();
	
	public S reduce(Symbol<V> ... symbols);
	
}
