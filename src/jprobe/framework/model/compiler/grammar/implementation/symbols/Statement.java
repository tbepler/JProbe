package jprobe.framework.model.compiler.grammar.implementation.symbols;

import jprobe.framework.model.compiler.grammar.Production;
import jprobe.framework.model.compiler.grammar.Symbol;
import jprobe.framework.model.compiler.grammar.implementation.Visitor;

public abstract class Statement extends Symbol<Visitor> implements Production<Visitor>{
	private static final long serialVersionUID = 1L;

	@Override
	public Class<? extends Symbol<Visitor>> getSymbolType() {
		return Statement.class;
	}
	
	@Override
	public Class<? extends Symbol<Visitor>> leftHandSide(){
		return Statement.class;
	}

}