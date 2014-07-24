package language.implementation.symbols;

import language.compiler.grammar.Symbol;
import language.implementation.Visitor;

public abstract class Statement extends Rule<Visitor>{
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