package language.implementation.symbols.patterns;

import language.compiler.grammar.Token;
import language.implementation.Visitor;
import language.implementation.symbols.Rule;

public abstract class Pattern extends Rule{
	private static final long serialVersionUID = 1L;


	@Override
	public Class<? extends Token<Visitor>> leftHandSide() {
		return Pattern.class;
	}


	@Override
	public Class<? extends Token<Visitor>> getSymbolType() {
		return Pattern.class;
	}

}
