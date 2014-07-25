package language.implementation.symbols;

import language.compiler.grammar.Token;
import language.implementation.Visitor;

public abstract class Statement extends Rule<Visitor>{
	private static final long serialVersionUID = 1L;
	
	protected Statement(){
		//TODO
		super(null);
	}

	@Override
	public Class<? extends Token<Visitor>> getSymbolType() {
		return Statement.class;
	}
	
	@Override
	public Class<? extends Token<Visitor>> leftHandSide(){
		return Statement.class;
	}

}