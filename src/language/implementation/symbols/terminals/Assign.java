package language.implementation.symbols.terminals;

import language.compiler.grammar.Symbol;
import language.implementation.Visitor;
import language.implementation.symbols.Constants;
import language.implementation.symbols.Terminal;

public final class Assign extends Terminal{
	private static final long serialVersionUID = 1L;

	@Override
	public String getRegex() {
		return Constants.ASSIGN_REGEX;
	}

	@Override
	public Symbol<Visitor> tokenize(String s) {
		return this;
	}

	@Override
	public void accept(Visitor visitor) {
		//do nothing
	}

	@Override
	public Class<? extends Symbol<Visitor>> getSymbolType() {
		return Assign.class;
	}

}
