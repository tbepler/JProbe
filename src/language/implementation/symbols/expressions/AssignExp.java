package language.implementation.symbols.expressions;

import java.util.List;

import language.compiler.ListUtil;
import language.compiler.grammar.Assoc;
import language.compiler.grammar.Token;
import language.implementation.Equals;
import language.implementation.Visitor;
import language.implementation.symbols.Constants;
import language.implementation.symbols.Declaration;
import language.implementation.symbols.Expression;
import language.implementation.symbols.terminals.Assign;

public class AssignExp extends Expression{
	private static final long serialVersionUID = 1L;
	
	private static final List<Class<? extends Token<Visitor>>> RHS =
			ListUtil.asUnmodifiableList(Declaration.class, Assign.class, Expression.class);
	
	public final Declaration left;
	public final Expression right;
	
	public AssignExp(Declaration left, Expression right){ this.left = left; this.right = right; }

	@Override
	public List<Class<? extends Token<Visitor>>> rightHandSide() {
		return RHS;
	}

	@Override
	public Token<Visitor> reduce(List<Token<Visitor>> symbols) {
		assert(symbols.size() == RHS.size());
		for(int i=0; i<RHS.size(); ++i){
			assert(RHS.get(i).isInstance(symbols.get(i)));
		}
		return new AssignExp((Declaration)symbols.get(0), (Expression)symbols.get(2));
	}
	
	@Override
	public Assoc getAssoc(){
		return Constants.ASSIGNMENT_ASSOC;
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
		if(o instanceof AssignExp){
			AssignExp that = (AssignExp) o;
			return Equals.equals(this.left, that.left) && Equals.equals(this.right, that.right);
		}
		return false;
	}

	
	
	
	
	
}
