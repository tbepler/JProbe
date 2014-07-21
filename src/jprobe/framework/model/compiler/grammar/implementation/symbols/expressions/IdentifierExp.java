package jprobe.framework.model.compiler.grammar.implementation.symbols.expressions;

import java.util.List;

import jprobe.framework.model.compiler.grammar.Symbol;
import jprobe.framework.model.compiler.grammar.implementation.ListUtil;
import jprobe.framework.model.compiler.grammar.implementation.SabreVisitor;
import jprobe.framework.model.compiler.grammar.implementation.symbols.Expression;
import jprobe.framework.model.compiler.grammar.implementation.symbols.terminals.Identifier;

public class IdentifierExp extends Expression{
	private static final long serialVersionUID = 1L;
	
	private static final List<Class<? extends Symbol<SabreVisitor>>> RHS =
			ListUtil.<Class<? extends Symbol<SabreVisitor>>>asUnmodifiableList(Identifier.class);
	
	public Identifier id;
	
	public IdentifierExp(Identifier id){ this.id = id; }

	@Override
	public void accept(SabreVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	public List<Class<? extends Symbol<SabreVisitor>>> rightHandSide() {
		return RHS;
	}

	@Override
	public Expression reduce(Symbol<SabreVisitor>... symbols) {
		assert(symbols.length == 1);
		assert(symbols[0] instanceof Identifier);
		return new IdentifierExp((Identifier) symbols[0]);
	}

}
