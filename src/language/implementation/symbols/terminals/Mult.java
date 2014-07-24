package language.implementation.symbols.terminals;

import language.compiler.grammar.Token;
import language.implementation.Visitor;
import language.implementation.symbols.Constants;
import language.implementation.symbols.Terminal;

public class Mult extends Terminal{
	private static final long serialVersionUID = 1L;
	
	private static final int HASH = 9935;

	@Override
	public String getRegex() {
		return Constants.MULT_REGEX;
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
	public Class<? extends Token<Visitor>> getSymbolType() {
		return Mult.class;
	}
	
	@Override
	public int getPriority(){
		return Constants.MULT_DIV_PRIORITY;
	}
	
	@Override
	public int hashCode(){
		return HASH;
	}
	
	@Override
	public boolean equals(Object o){
		if(o == null) return false;
		if(o == this) return true;
		return o instanceof Mult;
	}

}
