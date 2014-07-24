package language.compiler.lexer;

import language.compiler.grammar.Symbol;

public interface Tokenizer<V> {
	
	public String getRegex();
	
	public Symbol<V> tokenize(String s);
	
}
