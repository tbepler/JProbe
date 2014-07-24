package language.compiler.grammar;

import java.util.Collection;
import java.util.regex.Pattern;

public interface Grammar<V> extends Iterable<Production<V>>{
	
	public int getPrecedence(Class<? extends Symbol<V>> symbolType);
	
	public Pattern getTokenRegex();
	
	public Symbol<V> tokenize(String s);
	
	public Collection<Class<? extends Symbol<V>>> getTerminalSymbolTypes();
	
	public boolean isTerminal(Symbol<V> symbol);
	
	public boolean isTerminal(Class<? extends Symbol<V>> symbolType);
	
	public Collection<Production<V>> getProductions(Class<? extends Symbol<V>> leftHandSide);
	
	public Collection<Production<V>> getAllProductions();
	
	public Production<V> getStartProduction();
	
	public boolean isStartSymbol(Symbol<V> symbol);
	
	public boolean isEOFSymbol(Symbol<V> symbol);
	
	public boolean isEOFSymbol(Class<? extends Symbol<V>> symbolType);
	
	public Symbol<V> getEOFSymbol();
	
	
}
