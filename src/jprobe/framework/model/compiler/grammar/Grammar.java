package jprobe.framework.model.compiler.grammar;

import java.util.Collection;

public interface Grammar<V> extends Iterable<Production<V>>{
	
	public Collection<V> getTerminalSymbols();
	
	public boolean isTerminal(Symbol<V> symbol);
	
	public Collection<Production<V>> getProductions(Symbol<V> leftHandSide);
	
	public Collection<Production<V>> getAllProductions();
	
	public Production<V> getStartProduction();
	
	public boolean isEOFSymbol(Symbol<V> symbol);
	
	public Symbol<V> getEOFSymbol();
	
	
}
