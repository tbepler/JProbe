package language.implementation.symbols.terminals;

import language.compiler.grammar.Token;
import language.implementation.Visitor;
import language.implementation.symbols.Constants;
import language.implementation.symbols.Terminal;

public class DoubleLiteral extends Terminal{
	private static final long serialVersionUID = 1L;
	
	public final double n;
	
	public DoubleLiteral(double n){ this.n = n; }
	
	@Override
	public String getRegex() {
		return Constants.DOUBLE_REGEX;
	}

	@Override
	public Token<Visitor> tokenize(String s) {
		return new DoubleLiteral(Double.parseDouble(s));
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}

	@Override
	public Class<? extends Token<Visitor>> getSymbolType() {
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
