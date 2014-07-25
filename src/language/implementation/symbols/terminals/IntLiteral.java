package language.implementation.symbols.terminals;

import language.compiler.grammar.Token;
import language.implementation.Visitor;
import language.implementation.symbols.Constants;
import language.implementation.symbols.Terminal;

public class IntLiteral extends Terminal{
	private static final long serialVersionUID = 1L;

	public final int i;
	
	public IntLiteral(int i) {
		super(IntLiteral.class, Constants.INT_REGEX);
		this.i = i; }
	
	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}

	@Override
	public Token<Visitor> tokenize(String s) {
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
