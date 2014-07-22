package jprobe.framework.model.compiler.grammar.implementation.symbols.expressions;

import java.util.List;

import jprobe.framework.model.compiler.ListUtil;
import jprobe.framework.model.compiler.grammar.Symbol;
import jprobe.framework.model.compiler.grammar.implementation.SabreVisitor;
import jprobe.framework.model.compiler.grammar.implementation.symbols.Expression;
import jprobe.framework.model.compiler.grammar.implementation.symbols.terminals.CharLiteral;

public class CharExp extends Expression{
	private static final long serialVersionUID = 1L;
	
	private static final List<Class<? extends Symbol<SabreVisitor>>> RHS = 
			ListUtil.<Class<? extends Symbol<SabreVisitor>>>asUnmodifiableList(CharLiteral.class);
	
	public CharLiteral c;
	
	public CharExp(CharLiteral c){ this.c = c; }

	@Override
	public List<Class<? extends Symbol<SabreVisitor>>> rightHandSide() {
		return RHS;
	}

	@Override
	public Expression reduce(List<Symbol<SabreVisitor>> symbols) {
		assert(symbols.size() == 1);
		assert(symbols.get(0) instanceof CharLiteral);
		return new CharExp((CharLiteral) symbols.get(0));
	}

	@Override
	public void accept(SabreVisitor visitor) {
		visitor.visit(this);
	}

}
