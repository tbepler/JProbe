package jprobe.framework.model.compiler;

import java.util.regex.Pattern;

public abstract class Tokenizer<T> {
	
	private final Pattern m_Regex;
	
	public Tokenizer(Pattern pattern){
		m_Regex = pattern;
	}
	
	public Tokenizer(String regex){
		this(Pattern.compile(regex));
	}
	
	public Pattern getPattern(){
		return m_Regex;
	}
	
	abstract public Token<T> tokenize(String s);
	
}
