package language.implementation.symbols.expressions;

import java.util.List;

import language.compiler.ListUtil;
import language.compiler.grammar.Assoc;
import language.compiler.grammar.Token;
import language.implementation.Visitor;
import language.implementation.symbols.Constants;
import language.implementation.symbols.Expression;
import language.implementation.symbols.terminals.Exponent;

public class ExponentExp extends Expression{
	private static final long serialVersionUID = 1L;
	
	private static final List<Class<? extends Token<Visitor>>> RHS =
			ListUtil.asUnmodifiableList(Expression.class, Exponent.class, Expression.class);
	
	public final Expression left, right;
	
	public ExponentExp(Expression left, Expression right){this.left = left; this.right = right; }
	
	@Override
	public int getPriority(){
		return Constants.EXP_PRIORITY;
	}
	
	@Override
	public Assoc getAssoc(){
		return Constants.EXP_ASSOC;
	}

	@Override
	public List<Class<? extends Token<Visitor>>> rightHandSide() {
		return RHS;
	}

	@Override
	public Token<Visitor> reduce(List<Token<Visitor>> symbols) {
		assert(RHS.size() == symbols.size());
		for( int i = 0 ; i < RHS.size() ; ++i ){
			assert(RHS.get(i).equals(symbols.get(i).getSymbolType()));
		}
		return new ExponentExp((Expression) symbols.get(0), (Expression) symbols.get(2));
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}

}
