package jprobe.framework.model.compiler.grammar.implementation.symbols.terminals;

import jprobe.framework.model.compiler.grammar.Symbol;
import jprobe.framework.model.compiler.grammar.implementation.SabreVisitor;
import jprobe.framework.model.compiler.grammar.implementation.symbols.Terminal;

public class CharLiteral extends Terminal {
	private static final long serialVersionUID = 1L;
	
	public final char c;
	
	public CharLiteral(char c){ this.c = c; }

	@Override
	public void accept(SabreVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	public Class<? extends Symbol<SabreVisitor>> getSymbolType() {
		return CharLiteral.class;
	}

	@Override
	public String getRegex() {
		return Constants.CHAR_REGEX;
	}

	@Override
	public Symbol<SabreVisitor> tokenize(String s) {
		assert(s.length() == 3);
		return new CharLiteral(s.charAt(1));
	}
	
	@Override
	public String toString(){
		return "CHAR('"+c+"')";
	}
	
	@Override
	public int hashCode(){
		return new Character(c).hashCode();
	}
	
	@Override
	public boolean equals(Object o){
		if(o == null) return false;
		if(o == this) return true;
		if(o instanceof CharLiteral){
			return c == ((CharLiteral) o).c;
		}
		return false;
	}
	
}
