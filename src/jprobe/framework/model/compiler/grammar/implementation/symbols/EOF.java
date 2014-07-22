package jprobe.framework.model.compiler.grammar.implementation.symbols;

import jprobe.framework.model.compiler.grammar.Symbol;
import jprobe.framework.model.compiler.grammar.implementation.Visitor;

public final class EOF extends Symbol<Visitor>{
	private static final long serialVersionUID = 1L;
	
	private static final int HASH = 34920523;

	@Override
	public void accept(Visitor visitor) {
		//do nothing
	}

	@Override
	public Class<? extends Symbol<Visitor>> getSymbolType() {
		return EOF.class;
	}
	
	@Override
	public String toString(){
		return "EOF";
	}
	
	@Override
	public int hashCode(){
		return HASH;
	}
	
	@Override
	public boolean equals(Object o){
		if(o == null) return false;
		if(o == this) return true;
		return o instanceof EOF;
	}

}
