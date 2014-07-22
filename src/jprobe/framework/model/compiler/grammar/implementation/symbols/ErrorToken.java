package jprobe.framework.model.compiler.grammar.implementation.symbols;

import jprobe.framework.model.compiler.grammar.Symbol;
import jprobe.framework.model.compiler.grammar.implementation.Visitor;
import jprobe.framework.model.compiler.grammar.implementation.symbols.terminals.Constants;

public class ErrorToken extends Terminal{
	private static final long serialVersionUID = 1L;
	
	public String s;
	
	public ErrorToken(String s){ this.s = s; }

	@Override
	public String getRegex() {
		return Constants.ERROR_REGEX;
	}

	@Override
	public Symbol<Visitor> tokenize(String s) {
		return new ErrorToken(s);
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}

	@Override
	public Class<? extends Symbol<Visitor>> getSymbolType() {
		return ErrorToken.class;
	}
	
	@Override
	public String toString(){
		return "ERROR("+s+")";
	}

}
