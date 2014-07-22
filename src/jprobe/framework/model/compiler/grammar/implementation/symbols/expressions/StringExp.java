package jprobe.framework.model.compiler.grammar.implementation.symbols.expressions;

import java.util.List;

import jprobe.framework.model.compiler.ListUtil;
import jprobe.framework.model.compiler.grammar.Symbol;
import jprobe.framework.model.compiler.grammar.implementation.Equals;
import jprobe.framework.model.compiler.grammar.implementation.Visitor;
import jprobe.framework.model.compiler.grammar.implementation.symbols.Expression;
import jprobe.framework.model.compiler.grammar.implementation.symbols.terminals.StringLiteral;

public class StringExp extends Expression{
	private static final long serialVersionUID = 1L;
	
	private static final List<Class<? extends Symbol<Visitor>>> RHS = 
			ListUtil.<Class<? extends Symbol<Visitor>>>asUnmodifiableList(StringLiteral.class);
	
	public StringLiteral s;
	
	public StringExp(StringLiteral s){ this.s = s; }

	@Override
	public List<Class<? extends Symbol<Visitor>>> rightHandSide() {
		return RHS;
	}

	@Override
	public Expression reduce(List<Symbol<Visitor>> symbols) {
		assert(symbols.size() == 1);
		assert(symbols.get(0) instanceof StringLiteral);
		return new StringExp((StringLiteral) symbols.get(0));
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
				ToString.nestedToString(s) +
				"\n}";
	}
	*/
	
	@Override
	public int hashCode(){
		return Equals.hashCode(s);
	}
	
	@Override
	public boolean equals(Object o){
		if(o == null) return false;
		if(o == this) return true;
		if(o instanceof StringExp){
			StringExp that = (StringExp)o;
			return Equals.equals(this.s, that.s);
		}
		return false;
	}

}
