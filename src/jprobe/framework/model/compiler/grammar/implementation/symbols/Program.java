package jprobe.framework.model.compiler.grammar.implementation.symbols;

import java.util.List;

import jprobe.framework.model.compiler.grammar.Production;
import jprobe.framework.model.compiler.grammar.Symbol;
import jprobe.framework.model.compiler.grammar.implementation.ListUtil;
import jprobe.framework.model.compiler.grammar.implementation.SabreVisitor;

public class Program extends Symbol<SabreVisitor> implements Production<SabreVisitor>{
	private static final long serialVersionUID = 1L;

	private static final List<Class<? extends Symbol<SabreVisitor>>> RHS =
			ListUtil.asUnmodifiableList(Statement.class, EOF.class);
	
	public Statement stm;
	
	public Program(Statement stm){ this.stm = stm; }

	@Override
	public Class<? extends Symbol<SabreVisitor>> leftHandSide() {
		return Program.class;
	}

	@Override
	public List<Class<? extends Symbol<SabreVisitor>>> rightHandSide() {
		return RHS;
	}

	@Override
	public Symbol<SabreVisitor> reduce(Symbol<SabreVisitor>... symbols) {
		assert(symbols.length == 2);
		assert(symbols[0] instanceof Statement);
		assert(symbols[1] instanceof EOF);
		return new Program((Statement) symbols[0]);
	}

	@Override
	public void accept(SabreVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	public Class<? extends Symbol<SabreVisitor>> getSymbolType() {
		return Program.class;
	}

}
