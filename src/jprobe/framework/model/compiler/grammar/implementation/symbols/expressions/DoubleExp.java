package jprobe.framework.model.compiler.grammar.implementation.symbols.expressions;

import java.util.List;

import jprobe.framework.model.compiler.grammar.Symbol;
import jprobe.framework.model.compiler.grammar.implementation.ListUtil;
import jprobe.framework.model.compiler.grammar.implementation.SabreVisitor;
import jprobe.framework.model.compiler.grammar.implementation.symbols.Expression;
import jprobe.framework.model.compiler.grammar.implementation.symbols.terminals.DoubleLiteral;

public class DoubleExp extends Expression{
	private static final long serialVersionUID = 1L;
	
	private static final List<Class<? extends Symbol<SabreVisitor>>> RHS =
			ListUtil.<Class<? extends Symbol<SabreVisitor>>>asUnmodifiableList(DoubleLiteral.class);
	
	public DoubleLiteral n;
	
	public DoubleExp(DoubleLiteral n){ this.n = n; }

	@Override
	public List<Class<? extends Symbol<SabreVisitor>>> rightHandSide() {
		return RHS;
	}

	@Override
	public Expression reduce(Symbol<SabreVisitor>... symbols) {
		assert(symbols.length == 1);
		assert(symbols[0] instanceof DoubleLiteral);
		return new DoubleExp((DoubleLiteral) symbols[0]);
	}

	@Override
	public void accept(SabreVisitor visitor) {
		visitor.visit(this);
	}

}
