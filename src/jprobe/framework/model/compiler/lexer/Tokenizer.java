package jprobe.framework.model.compiler.lexer;

import java.util.regex.Pattern;

public interface Tokenizer<T> {
	
	public Pattern getPattern();
	
	public Token<T> tokenize(String s);
	
}
