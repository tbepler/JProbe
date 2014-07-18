package jprobe.framework.model.compiler;

import java.util.Collection;

public interface Grammar<S> extends Iterable<Production<S>>{
	
	public Collection<S> getTerminalSymbols();
	
	public boolean isTerminal(S symbol);
	
	public Collection<Production<S>> getProductions(S leftHandSide);
	
	public Collection<Production<S>> getAllProductions();
	
	
}
