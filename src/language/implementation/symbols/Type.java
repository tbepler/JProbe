package language.implementation.symbols;

import language.compiler.grammar.Token;
import language.implementation.Visitor;

public abstract class Type extends Rule<Visitor>{
	private static final long serialVersionUID = 1L;

	protected Type(Class<? extends Token<Visitor>> clazz,
			Class<? extends Token<Visitor>> ... rhs) {
		super(clazz, rhs);
	}

	@Override
	public Class<? extends Token<Visitor>> leftHandSide() {
		return Type.class;
	}

	@Override
	public Class<? extends Token<Visitor>> getSymbolType() {
		return Type.class;
	}

}
