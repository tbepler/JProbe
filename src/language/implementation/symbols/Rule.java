package language.implementation.symbols;

import language.compiler.grammar.Assoc;
import language.compiler.grammar.Production;
import language.compiler.grammar.Symbol;

public abstract class Rule<V> extends Symbol<V> implements Production<V> {
	private static final long serialVersionUID = 1L;
	
	@Override
	public int getPriority(){
		return Constants.DEFAULT_PRIORITY;
	}
	
	@Override
	public Assoc getAssoc(){
		return Constants.DEFAULT_ASSOC;
	}

}
