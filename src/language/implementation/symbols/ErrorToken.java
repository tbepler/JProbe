package language.implementation.symbols;

import language.compiler.grammar.Token;
import language.implementation.Visitor;

public class ErrorToken extends Terminal{
	private static final long serialVersionUID = 1L;
	
	public final String s;
	
	public ErrorToken(String s){
		super(ErrorToken.class, Constants.ERROR_REGEX);
		this.s = s; 
	}

	@Override
	public Token<Visitor> tokenize(String s) {
		return new ErrorToken(s);
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}
	
	@Override
	public String toString(){
		return "ERROR("+s+")";
	}

}
