package jprobe.framework.model.compiler.grammar.implementation.symbols.terminals;

import jprobe.framework.model.compiler.grammar.Symbol;
import jprobe.framework.model.compiler.grammar.implementation.SabreVisitor;
import jprobe.framework.model.compiler.grammar.implementation.symbols.Terminal;

public final class RParen extends Terminal{
	private static final long serialVersionUID = 1L;
	
	private static final int HASH = 913587;
	
	@Override
	public String getRegex() {
		return Constants.RPAREN_REGEX;
	}

	@Override
	public Symbol<SabreVisitor> tokenize(String s) {
		return this;
	}

	@Override
	public void accept(SabreVisitor visitor) {
		//do nothing
	}

	@Override
	public Class<? extends Symbol<SabreVisitor>> getSymbolType() {
		return RParen.class;
	}
	
	@Override
	public String toString(){
		return ")";
	}
	
	@Override
	public int hashCode(){
		return HASH;
	}
	
	@Override
	public boolean equals(Object o){
		if(o == null) return false;
		if(o == this) return true;
		return o instanceof RParen;
	}

}
