package language.implementation.symbols.expressions.assignment;

import language.compiler.grammar.Assoc;
import language.implementation.symbols.Constants;
import language.implementation.symbols.Expression;

public abstract class Assignment extends Expression{
	private static final long serialVersionUID = 1L;

	@Override
	public int getPriority(){
		return Constants.ASSIGNMENT_PRIORITY;
	}
	
	@Override
	public Assoc getAssoc(){
		return Constants.ASSIGNMENT_ASSOC;
	}
	
}
