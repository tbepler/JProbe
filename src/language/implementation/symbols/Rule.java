package language.implementation.symbols;

import language.compiler.grammar.Assoc;
import language.compiler.grammar.Production;
import language.compiler.grammar.Token;

public abstract class Rule<V> extends Token<V> implements Production<V> {
	private static final long serialVersionUID = 1L;
	
	@Override
	public int getPriority(){
		return Constants.DEFAULT_PRIORITY;
	}
	
	@Override
	public Assoc getAssoc(){
		return Constants.DEFAULT_ASSOC;
	}
	
	@Override
	public String toString(){
		return this.getClass().getSimpleName();
	}

}
