package language.implementation.symbols;

import language.compiler.grammar.Token;
import language.compiler.lexer.Tokenizer;
import language.implementation.Visitor;

public abstract class Terminal extends Token<Visitor> implements Tokenizer<Visitor>{
	private static final long serialVersionUID = 1L;
	
	public int getPriority(){
		return Constants.DEFAULT_PRIORITY;
	}
	
	@Override
	public String toString(){
		return this.getClass().getSimpleName();
	}
	
}
