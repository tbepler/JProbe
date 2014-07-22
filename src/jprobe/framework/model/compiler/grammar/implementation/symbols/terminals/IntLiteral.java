package jprobe.framework.model.compiler.grammar.implementation.symbols.terminals;

import jprobe.framework.model.compiler.grammar.Symbol;
import jprobe.framework.model.compiler.grammar.implementation.symbols.Terminal;
import jprobe.framework.model.compiler.grammar.implementation.Visitor;

public class IntLiteral extends Terminal{
	private static final long serialVersionUID = 1L;

	public final int i;
	
	public IntLiteral(int i) { this.i = i; }
	
	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}

	@Override
	public Class<? extends Symbol<Visitor>> getSymbolType() {
		return IntLiteral.class;
	}

	@Override
	public String getRegex() {
		return Constants.INT_REGEX;
	}

	@Override
	public Symbol<Visitor> tokenize(String s) {
		return new IntLiteral(Integer.parseInt(s));
	}
	
	@Override
	public String toString(){
		return "INT("+i+")";
	}
	
	@Override
	public int hashCode(){
		return new Integer(i).hashCode();
	}
	
	@Override
	public boolean equals(Object o){
		if(o == null) return false;
		if(o == this) return true;
		if(o instanceof IntLiteral){
			return i == ((IntLiteral) o).i;
		}
		return false;
	}

}
