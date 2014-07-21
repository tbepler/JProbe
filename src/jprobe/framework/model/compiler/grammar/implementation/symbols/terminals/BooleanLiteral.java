package jprobe.framework.model.compiler.grammar.implementation.symbols.terminals;

import jprobe.framework.model.compiler.grammar.Symbol;
import jprobe.framework.model.compiler.grammar.implementation.SabreVisitor;
import jprobe.framework.model.compiler.grammar.implementation.symbols.Terminal;

public class BooleanLiteral extends Terminal{
	private static final long serialVersionUID = 1L;
	
	public static final BooleanLiteral TRUE = new BooleanLiteral(true);
	public static final BooleanLiteral FALSE = new BooleanLiteral(false);
	
	public final boolean b;
	
	public BooleanLiteral(boolean b){ this.b = b; };
	
	@Override
	public String getRegex() {
		return Constants.BOOL_REGEX;
	}

	@Override
	public Symbol<SabreVisitor> tokenize(String s) {
		if(s.matches(Constants.TRUE_REGEX)){
			return TRUE;
		}
		return FALSE;
	}

	@Override
	public void accept(SabreVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	public Class<? extends Symbol<SabreVisitor>> getSymbolType() {
		return BooleanLiteral.class;
	}
	
	@Override
	public String toString(){
		return "BOOLEAN("+b+")";
	}
	
	@Override
	public int hashCode(){
		return new Boolean(b).hashCode();
	}
	
	@Override
	public boolean equals(Object o){
		if(o == null) return false;
		if(o == this) return true;
		if(o instanceof BooleanLiteral){
			return b == ((BooleanLiteral) o).b;
		}
		return false;
	}

}
