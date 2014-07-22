package jprobe.framework.model.compiler.grammar.implementation.symbols;

import jprobe.framework.model.compiler.grammar.Symbol;
import jprobe.framework.model.compiler.grammar.implementation.Visitor;
import jprobe.framework.model.compiler.grammar.implementation.symbols.terminals.Constants;

public class Whitespace extends Terminal{
	private static final long serialVersionUID = 1L;

	@Override
	public void accept(Visitor visitor) {
		//do nothing
	}

	@Override
	public Class<? extends Symbol<Visitor>> getSymbolType() {
		return Whitespace.class;
	}

	@Override
	public String getRegex() {
		return Constants.WHITESPACE_REGEX;
	}

	@Override
	public Symbol<Visitor> tokenize(String s) {
		//these tokens are ignored
		return null;
	}

}
