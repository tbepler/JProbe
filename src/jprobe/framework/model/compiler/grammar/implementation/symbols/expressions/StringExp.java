package jprobe.framework.model.compiler.grammar.implementation.symbols.expressions;

import java.util.List;

import jprobe.framework.model.compiler.ListUtil;
import jprobe.framework.model.compiler.grammar.Symbol;
import jprobe.framework.model.compiler.grammar.implementation.SabreVisitor;
import jprobe.framework.model.compiler.grammar.implementation.symbols.Expression;
import jprobe.framework.model.compiler.grammar.implementation.symbols.terminals.StringLiteral;

public class StringExp extends Expression{
	private static final long serialVersionUID = 1L;
	
	private static final List<Class<? extends Symbol<SabreVisitor>>> RHS = 
			ListUtil.<Class<? extends Symbol<SabreVisitor>>>asUnmodifiableList(StringLiteral.class);
	
	public StringLiteral s;
	
	public StringExp(StringLiteral s){ this.s = s; }

	@Override
	public List<Class<? extends Symbol<SabreVisitor>>> rightHandSide() {
		return RHS;
	}

	@Override
	public Expression reduce(List<Symbol<SabreVisitor>> symbols) {
		assert(symbols.size() == 1);
		assert(symbols.get(0) instanceof StringLiteral);
		return new StringExp((StringLiteral) symbols.get(0));
	}

	@Override
	public void accept(SabreVisitor visitor) {
		visitor.visit(this);
	}

}
