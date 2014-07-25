package language.implementation.symbols.terminals;

import language.compiler.grammar.Token;
import language.implementation.Visitor;
import language.implementation.symbols.Constants;
import language.implementation.symbols.Terminal;

public class FunKeyword extends Terminal{
	private static final long serialVersionUID = 1L;

	public FunKeyword() {
		super(FunKeyword.class, Constants.FUNC_DEF_REGEX);
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
		return "fun";
	}

}
