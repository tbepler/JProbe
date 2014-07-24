package language.implementation.symbols;

import language.compiler.grammar.Token;
import language.implementation.Visitor;

public abstract class Expression extends Rule<Visitor>{
	private static final long serialVersionUID = 1L;

	@Override
	public Class<? extends Token<Visitor>> getSymbolType() {
		return Expression.class;
	}

	@Override
	public Class<Expression> leftHandSide() {
		return Expression.class;
	}

}
