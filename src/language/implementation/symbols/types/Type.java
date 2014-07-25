package language.implementation.symbols.types;

import language.compiler.grammar.Token;
import language.implementation.Visitor;
import language.implementation.symbols.Rule;

public abstract class Type extends Rule{
	private static final long serialVersionUID = 1L;

	@Override
	public Class<? extends Token<Visitor>> leftHandSide() {
		return Type.class;
	}

	@Override
	public Class<? extends Token<Visitor>> getSymbolType() {
		return Type.class;
	}

}
