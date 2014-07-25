package language.implementation.symbols;

import language.compiler.grammar.Token;
import language.implementation.Visitor;

public class Whitespace extends Terminal{
	private static final long serialVersionUID = 1L;

	public Whitespace() {
		super(Whitespace.class, Constants.WHITESPACE_REGEX);
	}

	@Override
	public void accept(Visitor visitor) {
		//do nothing
	}

	@Override
	public Token<Visitor> tokenize(String s) {
		//these tokens are ignored
		return null;
	}

}
