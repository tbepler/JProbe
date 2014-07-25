package language.implementation.symbols.expressions.literal;

import java.util.List;

import language.compiler.ListUtil;
import language.compiler.grammar.Token;
import language.implementation.Equals;
import language.implementation.Visitor;
import language.implementation.symbols.expressions.Expression;
import language.implementation.symbols.terminals.IntLiteral;

public class IntExp extends Expression{
	private static final long serialVersionUID = 1L;
	
	private static final List<Class<? extends Token<Visitor>>> RHS =
			ListUtil.<Class<? extends Token<Visitor>>>asUnmodifiableList(IntLiteral.class);
	
	public IntLiteral n;
	
	public IntExp(IntLiteral n){ this.n = n; }

	@Override
	public List<Class<? extends Token<Visitor>>> rightHandSide() {
		return RHS;
	}

	@Override
	public Expression reduce(List<Token<Visitor>> symbols) {
		assert(symbols.size() == 1);
		assert(symbols.get(0) instanceof IntLiteral);
		return new IntExp((IntLiteral) symbols.get(0));
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
				ToString.nestedToString(n) +
				"\n}";
	}
	*/
	
	@Override
	public int hashCode(){
		return Equals.hashCode(n);
	}
	
	@Override
	public boolean equals(Object o){
		if(o == null) return false;
		if(o == this) return true;
		if(o instanceof IntExp){
			IntExp that = (IntExp)o;
			return Equals.equals(this.n, that.n);
		}
		return false;
	}

}
