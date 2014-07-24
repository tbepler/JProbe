package language.compiler.grammar;

import java.util.List;

public interface Production<V> {
	
	public Class<? extends Symbol<V>> leftHandSide();
	
	public List<Class<? extends Symbol<V>>> rightHandSide();
	
	public Symbol<V> reduce(List<Symbol<V>> symbols);
	
	public int getPriority();
	
	public Assoc getAssoc();
	
}
