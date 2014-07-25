package language.implementation.symbols.expressions;

import language.compiler.grammar.Token;
import language.implementation.Visitor;
import language.implementation.symbols.Rule;

public abstract class Expression extends Rule{
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
