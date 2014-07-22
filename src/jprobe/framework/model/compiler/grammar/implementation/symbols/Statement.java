package jprobe.framework.model.compiler.grammar.implementation.symbols;

import jprobe.framework.model.compiler.grammar.Production;
import jprobe.framework.model.compiler.grammar.Symbol;
import jprobe.framework.model.compiler.grammar.implementation.SabreVisitor;

public abstract class Statement extends Symbol<SabreVisitor> implements Production<SabreVisitor>{
	private static final long serialVersionUID = 1L;

	@Override
	public Class<? extends Symbol<SabreVisitor>> getSymbolType() {
		return Statement.class;
	}

}