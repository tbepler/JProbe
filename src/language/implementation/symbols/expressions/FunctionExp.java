package language.implementation.symbols.expressions;

import java.util.List;

import language.compiler.ListUtil;
import language.compiler.grammar.Assoc;
import language.compiler.grammar.Token;
import language.implementation.Equals;
import language.implementation.Visitor;
import language.implementation.symbols.Constants;

/**
 * Function application
 * 
 * @author Tristan Bepler
 *
 */
public class FunctionExp extends Expression{
	private static final long serialVersionUID = 1L;
	
	private static final List<Class<? extends Token<Visitor>>> RHS =
			ListUtil.<Class<? extends Token<Visitor>>>asUnmodifiableList(Expression.class, Expression.class);
	
	public final Expression left,right;
	
	public FunctionExp(Expression left, Expression right){ this.left = left; this.right = right; }

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}

	@Override
	public List<Class<? extends Token<Visitor>>> rightHandSide() {
		return RHS;
	}

	@Override
	public Expression reduce(List<Token<Visitor>> symbols) {
		assert(RHS.size() == symbols.size());
		for( int i = 0 ; i < RHS.size() ; ++i ){
			assert(RHS.get(i).equals(symbols.get(i).getSymbolType()));
		}
		return new FunctionExp((Expression) symbols.get(0), (Expression) symbols.get(1));
	}
	
	@Override
	public int getPriority(){
		return Constants.FUNCTION_APPLICATION_PRIORITY;
	}
	
	@Override
	public Assoc getAssoc(){
		return Constants.FUNCTION_APPLICATION_ASSOC;
	}
	
	/*
	@Override
	public String toString(){
		return this.getClass().getSimpleName() +
				"\n{\n" +
				ToString.nestedToString(id) +
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
		if(o instanceof FunctionExp){
			FunctionExp that = (FunctionExp)o;
			return Equals.equals(this.left, that.left) && Equals.equals(this.right, that.right);
		}
		return false;
	}

}
