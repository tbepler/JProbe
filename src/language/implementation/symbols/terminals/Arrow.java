package language.implementation.symbols.terminals;

import language.compiler.grammar.Token;
import language.implementation.Visitor;
import language.implementation.symbols.Constants;
import language.implementation.symbols.Terminal;

public class Arrow extends Terminal{
	private static final long serialVersionUID = 1L;

	public Arrow() {
		super(Arrow.class, Constants.ARROW_REGEX);
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
	public String toString(){
		return Constants.ARROW_REGEX;
	}
	
	@Override
	public int hashCode(){
		return 29385402;
	}
	
	@Override
	public boolean equals(Object o){
		if(o == this) return true;
		if(o == null) return false;
		return o instanceof Arrow;
	}

}
