package jprobe.framework.model.compiler.grammar.implementation.symbols.expressions;

import java.util.List;

import jprobe.framework.model.compiler.ListUtil;
import jprobe.framework.model.compiler.grammar.Symbol;
import jprobe.framework.model.compiler.grammar.implementation.Equals;
import jprobe.framework.model.compiler.grammar.implementation.Visitor;
import jprobe.framework.model.compiler.grammar.implementation.symbols.Expression;
import jprobe.framework.model.compiler.grammar.implementation.symbols.terminals.BooleanLiteral;

public class BooleanExp extends Expression{
	private static final long serialVersionUID = 1L;
	
	private static final List<Class<? extends Symbol<Visitor>>> RHS = 
			ListUtil.<Class<? extends Symbol<Visitor>>>asUnmodifiableList(BooleanLiteral.class);
	
	public final BooleanLiteral b;
	
	public BooleanExp(BooleanLiteral b){ this.b = b; }

	@Override
	public List<Class<? extends Symbol<Visitor>>> rightHandSide() {
		return RHS;
	}

	@Override
	public Expression reduce(List<Symbol<Visitor>> symbols) {
		assert(symbols.size() == 1);
		assert(symbols.get(0) instanceof BooleanLiteral);
		return new BooleanExp((BooleanLiteral) symbols.get(0));
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}
	
	/*
	@Override
	public String toString(){
		return BooleanExp.class.getSimpleName() +
				"\n{\n" +
				ToString.nestedToString(b) +
				"\n}";
	}
	*/
	
	@Override
	public int hashCode(){
		return Equals.hashCode(b);
	}
	
	@Override
	public boolean equals(Object o){
		if(o == null) return false;
		if(o == this) return true;
		if(o instanceof BooleanExp){
			BooleanExp that = (BooleanExp)o;
			return Equals.equals(this.b, that.b);
		}
		return false;
	}

}
