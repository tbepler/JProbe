package jprobe.framework.model.compiler.grammar.implementation.symbols.expressions;

import java.util.List;

import jprobe.framework.model.compiler.ListUtil;
import jprobe.framework.model.compiler.grammar.Symbol;
import jprobe.framework.model.compiler.grammar.implementation.Equals;
import jprobe.framework.model.compiler.grammar.implementation.Visitor;
import jprobe.framework.model.compiler.grammar.implementation.symbols.Expression;
import jprobe.framework.model.compiler.grammar.implementation.symbols.terminals.DoubleLiteral;

public class DoubleExp extends Expression{
	private static final long serialVersionUID = 1L;
	
	private static final List<Class<? extends Symbol<Visitor>>> RHS =
			ListUtil.<Class<? extends Symbol<Visitor>>>asUnmodifiableList(DoubleLiteral.class);
	
	public DoubleLiteral n;
	
	public DoubleExp(DoubleLiteral n){ this.n = n; }

	@Override
	public List<Class<? extends Symbol<Visitor>>> rightHandSide() {
		return RHS;
	}

	@Override
	public Expression reduce(List<Symbol<Visitor>> symbols) {
		assert(symbols.size() == 1);
		assert(symbols.get(0) instanceof DoubleLiteral);
		return new DoubleExp((DoubleLiteral) symbols.get(0));
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
		if(o instanceof DoubleExp){
			DoubleExp that = (DoubleExp)o;
			return Equals.equals(this.n, that.n);
		}
		return false;
	}

}
