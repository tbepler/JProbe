package jprobe.framework.model.compiler.grammar.implementation.symbols;

import java.util.List;

import jprobe.framework.model.compiler.ListUtil;
import jprobe.framework.model.compiler.grammar.Production;
import jprobe.framework.model.compiler.grammar.Symbol;
import jprobe.framework.model.compiler.grammar.implementation.Equals;
import jprobe.framework.model.compiler.grammar.implementation.Visitor;

public class Program extends Symbol<Visitor> implements Production<Visitor>{
	private static final long serialVersionUID = 1L;

	private static final List<Class<? extends Symbol<Visitor>>> RHS =
			ListUtil.asUnmodifiableList(Statement.class, EOF.class);
	
	public final Statement stm;
	
	public Program(Statement stm){ this.stm = stm; }

	@Override
	public Class<? extends Symbol<Visitor>> leftHandSide() {
		return Program.class;
	}

	@Override
	public List<Class<? extends Symbol<Visitor>>> rightHandSide() {
		return RHS;
	}

	@Override
	public Symbol<Visitor> reduce(List<Symbol<Visitor>> symbols) {
		assert(symbols.size() == 2);
		assert(symbols.get(0) instanceof Statement);
		assert(symbols.get(1) instanceof EOF);
		return new Program((Statement) symbols.get(0));
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}

	@Override
	public Class<? extends Symbol<Visitor>> getSymbolType() {
		return Program.class;
	}
	
	/*
	@Override
	public String toString(){
		return 	Program.class.getSimpleName()+
				"\n{\n" +
				ToString.nestedToString(stm) +
				"\n}";
	}
	*/
	
	@Override
	public int hashCode(){
		return Equals.hashCode(stm);
	}
	
	@Override
	public boolean equals(Object o){
		if(o == null) return false;
		if(o == this) return true;
		if(o instanceof Program){
			return Equals.equals(stm, ((Program)o).stm);
		}
		return false;
	}

	
	
	
	
	
}
