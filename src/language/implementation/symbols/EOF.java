package language.implementation.symbols;

import language.compiler.grammar.Token;
import language.implementation.Visitor;

public final class EOF extends Terminal{

	private static final long serialVersionUID = 1L;
	
	private static final int HASH = 34920523;

	public EOF() {
		super(EOF.class, null);
	}

	@Override
	public void accept(Visitor visitor) {
		//do nothing
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
	public Token<Visitor> tokenize(String s) {
		return null;
	}

}
