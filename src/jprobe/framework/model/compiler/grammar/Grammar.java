package jprobe.framework.model.compiler.grammar;

import java.util.Collection;
import java.util.List;

import jprobe.framework.model.compiler.lexer.Tokenizer;

public interface Grammar<V> extends Iterable<Production<V>>{
	
	public List<? extends Tokenizer<V>> getTokenizers();
	
	public Collection<? extends Symbol<V>> getTerminalSymbols();
	
	public boolean isTerminal(Symbol<V> symbol);
	
	public Collection<Production<V>> getProductions(Class<? extends Symbol<V>> leftHandSide);
	
	public Collection<Production<V>> getAllProductions();
	
	public Production<V> getStartProduction();
	
	public boolean isEOFSymbol(Symbol<V> symbol);
	
	public Symbol<V> getEOFSymbol();
	
	
}
