package language.implementation.symbols;

import java.util.List;

import language.compiler.ListUtil;
import language.compiler.grammar.Token;
import language.implementation.Equals;
import language.implementation.Visitor;

public class Program extends Rule<Visitor>{
	private static final long serialVersionUID = 1L;

	private static final List<Class<? extends Token<Visitor>>> RHS =
			ListUtil.asUnmodifiableList(Statement.class, EOF.class);
	
	public final Statement stm;
	
	public Program(Statement stm){ /*TODO*/ super(null); this.stm = stm; }

	@Override
	public Class<? extends Token<Visitor>> leftHandSide() {
		return Program.class;
	}

	@Override
	public List<Class<? extends Token<Visitor>>> rightHandSide() {
		return RHS;
	}

	@Override
	public Token<Visitor> reduce(List<Token<Visitor>> symbols) {
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
	public Class<? extends Token<Visitor>> getSymbolType() {
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
