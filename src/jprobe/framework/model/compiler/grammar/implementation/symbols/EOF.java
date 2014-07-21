package jprobe.framework.model.compiler.grammar.implementation.symbols;

import jprobe.framework.model.compiler.grammar.Symbol;
import jprobe.framework.model.compiler.grammar.implementation.SabreVisitor;

public class EOF extends Symbol<SabreVisitor>{
	private static final long serialVersionUID = 1L;

	@Override
	public void accept(SabreVisitor visitor) {
		//do nothing
	}

	@Override
	public Class<? extends Symbol<SabreVisitor>> getSymbolType() {
		return EOF.class;
	}

}
