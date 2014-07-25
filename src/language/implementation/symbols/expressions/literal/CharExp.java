package language.implementation.symbols.expressions.literal;

import java.util.List;

import language.compiler.ListUtil;
import language.compiler.grammar.Token;
import language.implementation.Equals;
import language.implementation.Visitor;
import language.implementation.symbols.expressions.Expression;
import language.implementation.symbols.terminals.CharLiteral;

public class CharExp extends Expression{
	private static final long serialVersionUID = 1L;
	
	private static final List<Class<? extends Token<Visitor>>> RHS = 
			ListUtil.<Class<? extends Token<Visitor>>>asUnmodifiableList(CharLiteral.class);
	
	public final CharLiteral c;
	
	public CharExp(CharLiteral c){ this.c = c; }

	@Override
	public List<Class<? extends Token<Visitor>>> rightHandSide() {
		return RHS;
	}

	@Override
	public Expression reduce(List<Token<Visitor>> symbols) {
		assert(symbols.size() == 1);
		assert(symbols.get(0) instanceof CharLiteral);
		return new CharExp((CharLiteral) symbols.get(0));
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}
	
	/*
	@Override
	public String toString(){
		return this.getClass().getSimpleName() +
				"\n{\n" +
				ToString.nestedToString(c) +
				"\n}";
	}
	*/
	
	@Override
	public int hashCode(){
		return Equals.hashCode(c);
	}
	
	@Override
	public boolean equals(Object o){
		if(o == null) return false;
		if(o == this) return true;
		if(o instanceof CharExp){
			CharExp that = (CharExp)o;
			return Equals.equals(this.c, that.c);
		}
		return false;
	}

}
