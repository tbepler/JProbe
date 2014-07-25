package language.implementation.symbols.terminals;

import language.compiler.grammar.Token;
import language.implementation.Visitor;
import language.implementation.symbols.Constants;
import language.implementation.symbols.Terminal;

public final class Assign extends Terminal{
	private static final long serialVersionUID = 1L;

	public Assign() {
		super(Assign.class, Constants.ASSIGN_REGEX);
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
	public int hashCode(){
		return 135315;
	}
	
	@Override
	public boolean equals(Object o){
		if(o == this) return true;
		if(o == null) return false;
		return o instanceof Assign;
	}

}
