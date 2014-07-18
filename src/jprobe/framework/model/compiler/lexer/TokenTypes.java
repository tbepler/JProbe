package jprobe.framework.model.compiler.lexer;

import java.util.regex.Pattern;

public enum TokenTypes implements Tokenizer<TokenTypes>{
	
	ID( "[a-zA-Z_]+[a-zA-Z0-9_]*" , false),
	NUM( "-?[0-9]+(\\.[0-9]+)?" , false),
	STR( "\"(?:\\\\\"|[^\"])*?\"" , false),
	CHAR( "'.'", false),
	L_PAREN( "\\(", false),
	R_PAREN( "\\)", false),
	COMMA( ",", false),
	L_BRACKET( "\\[", false),
	R_BRACKET( "\\]", false),
	SEMICOLON( ";", false),
	WHITESPACE( "\\s+", true),
	ERROR( ".", false);
	
	private final Pattern m_Pattern;
	private final boolean m_Ignored;
	
	private TokenTypes(String regex, boolean ignored){
		m_Pattern = Pattern.compile(regex);
		m_Ignored = ignored;
	}

	@Override
	public Pattern getPattern() {
		return m_Pattern;
	}

	@Override
	public Token<TokenTypes> tokenize(String s) {
		if(m_Ignored) return null;
		return new Token<TokenTypes>(s,this);
	}

}
