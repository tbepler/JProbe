package jprobe.framework.model.compiler.lexer;

import jprobe.framework.model.compiler.grammar.Symbol;

public interface Tokenizer<V> {
	
	public String getRegex();
	
	public Symbol<V> tokenize(String s);
	
}
