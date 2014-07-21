package jprobe.framework.model.compiler.grammar.implementation.symbols.terminals;

import jprobe.framework.model.compiler.grammar.Symbol;
import jprobe.framework.model.compiler.grammar.implementation.SabreVisitor;
import jprobe.framework.model.compiler.grammar.implementation.symbols.Terminal;

public final class Semicolon extends Terminal{
	private static final long serialVersionUID = 1L;
	
	private static final int HASH = 1253683;
	
	@Override
	public String getRegex() {
		return Constants.SEMICOLON_REGEX;
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
		return Semicolon.class;
	}
	
	@Override
	public String toString(){
		return ";";
	}
	
	@Override
	public int hashCode(){
		return HASH;
	}
	
	@Override
	public boolean equals(Object o){
		if(o == null) return false;
		if(o == this) return true;
		return o instanceof Semicolon;
	}

}
