package language.implementation.symbols;

import language.compiler.grammar.Symbol;
import language.implementation.Visitor;

public final class EOF extends Terminal{
	private static final long serialVersionUID = 1L;
	
	private static final int HASH = 34920523;

	@Override
	public void accept(Visitor visitor) {
		//do nothing
	}

	@Override
	public Class<? extends Symbol<Visitor>> getSymbolType() {
		return EOF.class;
	}
	
	@Override
	public String toString(){
		return "EOF";
	}
	
	@Override
	public int hashCode(){
		return HASH;
	}
	
	@Override
	public boolean equals(Object o){
		if(o == null) return false;
		if(o == this) return true;
		return o instanceof EOF;
	}

	@Override
	public String getRegex() {
		return null;
	}

	@Override
	public Symbol<Visitor> tokenize(String s) {
		return null;
	}

}
