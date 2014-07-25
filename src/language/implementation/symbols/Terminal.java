package language.implementation.symbols;

import language.compiler.grammar.Token;
import language.compiler.lexer.Tokenizer;
import language.implementation.Visitor;

public abstract class Terminal extends Token<Visitor> implements Tokenizer<Visitor>{
	private static final long serialVersionUID = 1L;
	
	private final String regex;
	private final Class<? extends Terminal> clazz;
	
	protected Terminal(Class<? extends Terminal> clazz, String regex){
		this.regex = regex;
		this.clazz = clazz;
	}
	
	public int getPriority(){
		return Constants.DEFAULT_PRIORITY;
	}
	
	@Override
	public String toString(){
		return this.getClass().getSimpleName();
	}

	@Override
	public String getRegex() {
		return regex;
	}

	@Override
	public Class<? extends Token<Visitor>> getSymbolType() {
		return clazz;
	}
	
}
