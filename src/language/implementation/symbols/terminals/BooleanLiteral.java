package language.implementation.symbols.terminals;

import language.compiler.grammar.Token;
import language.implementation.Visitor;
import language.implementation.symbols.Constants;
import language.implementation.symbols.Terminal;

public class BooleanLiteral extends Terminal{
	private static final long serialVersionUID = 1L;
	
	public static final BooleanLiteral TRUE = new BooleanLiteral(true);
	public static final BooleanLiteral FALSE = new BooleanLiteral(false);
	
	public final boolean b;
	
	public BooleanLiteral(boolean b){ 
		super(BooleanLiteral.class, Constants.BOOL_REGEX);
		this.b = b;
	};

	@Override
	public Token<Visitor> tokenize(String s) {
		if(s.matches(Constants.TRUE_REGEX)){
			return TRUE;
		}
		return FALSE;
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
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
