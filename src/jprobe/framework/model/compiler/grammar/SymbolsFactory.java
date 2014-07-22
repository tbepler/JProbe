package jprobe.framework.model.compiler.grammar;

import java.util.List;

public interface SymbolsFactory<V> {
	
	public Symbol<V> newEOFSymbol();
	public Production<V> newStartProduction();
	public List<Production<V>> newProductions();
	public List<? extends Symbol<V>> newTerminals();
	
}
