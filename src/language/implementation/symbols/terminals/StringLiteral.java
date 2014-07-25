package language.implementation.symbols.terminals;

import language.compiler.grammar.Token;
import language.implementation.Visitor;
import language.implementation.symbols.Constants;
import language.implementation.symbols.Terminal;

public class StringLiteral extends Terminal{
	private static final long serialVersionUID = 1L;
	
	public final String s;
	
	public StringLiteral(String s){
		super(StringLiteral.class, Constants.STR_REGEX);
		this.s = s;
	}
	
	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}
	
	private static String process(String s){
		//ignore the first and last chars
		int len = s.length() - 1;
		StringBuilder builder = new StringBuilder();
		for(int i=1; i<len; ++i){
			//remove escape chars
			if(s.charAt(i) == Constants.ESC){
				if(++i >= len){
					break;
				}
			}
			builder.append(s.charAt(i));
		}
		return builder.toString();
	}
	
	@Override
	public Token<Visitor> tokenize(String s) {
		return new StringLiteral(process(s));
	}
	
	@Override
	public String toString(){
		return "STR("+s+")";
	}
	
	@Override
	public int hashCode(){
		return s.hashCode();
	}
	
	@Override
	public boolean equals(Object o){
		if(o == null) return false;
		if(o == this) return true;
		if(o instanceof StringLiteral){
			return s.equals(((StringLiteral) o).s);
		}
		return false;
	}
	
	

}
