package jprobe.framework.model.compiler.grammar.implementation.symbols.terminals;

import jprobe.framework.model.compiler.grammar.Symbol;
import jprobe.framework.model.compiler.grammar.implementation.SabreVisitor;
import jprobe.framework.model.compiler.grammar.implementation.symbols.Terminal;

public class DoubleLiteral extends Terminal{
	private static final long serialVersionUID = 1L;
	
	public final double n;
	
	public DoubleLiteral(double n){ this.n = n; }
	
	@Override
	public String getRegex() {
		return Constants.DOUBLE_REGEX;
	}

	@Override
	public Symbol<SabreVisitor> tokenize(String s) {
		return new DoubleLiteral(Double.parseDouble(s));
	}

	@Override
	public void accept(SabreVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	public Class<? extends Symbol<SabreVisitor>> getSymbolType() {
		return DoubleLiteral.class;
	}
	
	@Override
	public String toString(){
		return "DOUBLE("+n+")";
	}
	
	@Override
	public int hashCode(){
		return new Double(n).hashCode();
	}
	
	@Override
	public boolean equals(Object o){
		if(o == null) return false;
		if(o == this) return true;
		if(o instanceof DoubleLiteral){
			return n == ((DoubleLiteral) o).n;
		}
		return false;
	}

}
