package language.implementation.symbols.statements;

import java.util.List;

import language.compiler.ListUtil;
import language.compiler.grammar.Token;
import language.implementation.Equals;
import language.implementation.Visitor;
import language.implementation.symbols.Expression;
import language.implementation.symbols.Statement;
import language.implementation.symbols.terminals.Semicolon;

public class ExpStm extends Statement{
	private static final long serialVersionUID = 1L;
	
	private static final List<Class<? extends Token<Visitor>>> RHS =
			ListUtil.asUnmodifiableList(Expression.class, Semicolon.class);
	
	public final Expression e;
	
	public ExpStm(Expression e){ this.e = e; }

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
		return new ExpStm((Expression)symbols.get(0));
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
		return Equals.hashCode(e);
	}
	
	@Override
	public boolean equals(Object o){
		if(o == null) return false;
		if(o == this) return true;
		if(o instanceof ExpStm){
			ExpStm that = (ExpStm) o;
			return Equals.equals(this.e, that.e);
		}
		return false;
	}

	
	
	
	
	
}
