package language.implementation.symbols.expressions;

import java.util.List;

import language.compiler.ListUtil;
import language.compiler.grammar.Assoc;
import language.compiler.grammar.Symbol;
import language.implementation.Visitor;
import language.implementation.symbols.Constants;
import language.implementation.symbols.Expression;
import language.implementation.symbols.terminals.Mult;

public class MultExp extends Expression{
	private static final long serialVersionUID = 1L;
	
	private static final List<Class<? extends Symbol<Visitor>>> RHS =
			ListUtil.asUnmodifiableList(Expression.class, Mult.class, Expression.class);
	
	public final Expression left, right;
	
	public MultExp(Expression left, Expression right){this.left = left; this.right = right; }
	
	@Override
	public int getPriority(){
		return Constants.MULT_DIV_PRIORITY;
	}
	
	@Override
	public Assoc getAssoc(){
		return Constants.PLUS_MINUS_MULT_DIV_ASSOC;
	}

	@Override
	public List<Class<? extends Symbol<Visitor>>> rightHandSide() {
		return RHS;
	}

	@Override
	public Symbol<Visitor> reduce(List<Symbol<Visitor>> symbols) {
		assert(RHS.size() == symbols.size());
		for( int i = 0 ; i < RHS.size() ; ++i ){
			assert(RHS.get(i).equals(symbols.get(i).getSymbolType()));
		}
		return new MultExp((Expression) symbols.get(0), (Expression) symbols.get(2));
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}

}
