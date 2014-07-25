package language.implementation.symbols.terminals;

import language.compiler.grammar.Token;
import language.implementation.Visitor;
import language.implementation.symbols.Constants;
import language.implementation.symbols.Terminal;

public final class Comma extends Terminal{
	private static final long serialVersionUID = 1L;

	private static final int HASH = 256924;
	
	public Comma() {
		super(Comma.class, Constants.COMMA_REGEX);
	}

	@Override
	public Token<Visitor> tokenize(String s) {
		return this;
	}

	@Override
	public void accept(Visitor visitor) {
		//do nothing
	}
	
	@Override
	public int getPriority(){
		return Constants.LIST_APPEND_PRIORITY;
	}
	
	@Override
	public String toString(){
		return Constants.COMMA_REGEX;
	}
	
	@Override
	public int hashCode(){
		return HASH;
	}
	
	@Override
	public boolean equals(Object o){
		if(o == null) return false;
		if(o == this) return true;
		return o instanceof Comma;
	}

}
