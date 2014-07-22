package jprobe.framework.model.compiler.grammar.implementation.symbols.statements;

import java.util.List;

import jprobe.framework.model.compiler.ListUtil;
import jprobe.framework.model.compiler.grammar.Symbol;
import jprobe.framework.model.compiler.grammar.implementation.Equals;
import jprobe.framework.model.compiler.grammar.implementation.Visitor;
import jprobe.framework.model.compiler.grammar.implementation.symbols.Expression;
import jprobe.framework.model.compiler.grammar.implementation.symbols.Statement;
import jprobe.framework.model.compiler.grammar.implementation.symbols.terminals.Assign;
import jprobe.framework.model.compiler.grammar.implementation.symbols.terminals.Identifier;

public class AssignStm extends Statement{
	private static final long serialVersionUID = 1L;
	
	private static final List<Class<? extends Symbol<Visitor>>> RHS =
			ListUtil.asUnmodifiableList(Identifier.class, Assign.class, Expression.class);
	
	public final Identifier left;
	public final Expression right;
	
	public AssignStm(Identifier left, Expression right){ this.left = left; this.right = right; }

	@Override
	public List<Class<? extends Symbol<Visitor>>> rightHandSide() {
		return RHS;
	}

	@Override
	public Symbol<Visitor> reduce(List<Symbol<Visitor>> symbols) {
		assert(symbols.size() == RHS.size());
		for(int i=0; i<RHS.size(); ++i){
			assert(RHS.get(i).isInstance(symbols.get(i)));
		}
		return new AssignStm((Identifier)symbols.get(0), (Expression)symbols.get(2));
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}
	
	/*
	@Override
	public String toString(){
		return AssignStm.class.getSimpleName()+
				"\n{\n"+
				ToString.nestedToString(left, right)+
				"\n}";
	}
	*/
	
	@Override
	public int hashCode(){
		return Equals.hashCode(left, right);
	}
	
	@Override
	public boolean equals(Object o){
		if(o == null) return false;
		if(o == this) return true;
		if(o instanceof AssignStm){
			AssignStm that = (AssignStm) o;
			return Equals.equals(this.left, that.left) && Equals.equals(this.right, that.right);
		}
		return false;
	}

	
	
	
	
	
}
