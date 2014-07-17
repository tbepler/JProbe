package jprobe.framework.model.compiler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {
	
	private final Collection<Tokenizer> m_Tokenizers;
	private Pattern m_TokenPattern;
	
	public Lexer(){
		m_Tokenizers = new ArrayList<Tokenizer>();
		m_TokenPattern = null;
	}
	
	public Lexer(Collection<Tokenizer> tokenizers){
		m_Tokenizers = new ArrayList<Tokenizer>(tokenizers);
		m_TokenPattern = buildPattern(tokenizers);
	}
	
	public void addTokenizer(Tokenizer t){
		m_Tokenizers.add(t);
		m_TokenPattern = appendPattern(m_TokenPattern, t.getPattern());
	}
	
	public void removeTokenizer(Tokenizer t){
		m_Tokenizers.remove(t);
		m_TokenPattern = buildPattern(m_Tokenizers);
	}
	
	public List<Token> analyze(String s){
		List<Token> tokens = new ArrayList<Token>();
		Matcher tokenMatcher = this.tokenPattern().matcher(s);
		System.out.println(this.tokenPattern().pattern());
		while(tokenMatcher.find()){
			tokens.add(this.tokenize(tokenMatcher.group()));
		}
		return tokens;
	}
	
	private Token tokenize(String s){
		for(Tokenizer t : m_Tokenizers){
			if(t.getPattern().matcher(s).matches()){
				return t.tokenize(s);
			}
		}
		throw new LexingException("String \""+s+"\" does not match any known token patterns.");
	}
	
	private Pattern tokenPattern(){
		return m_TokenPattern;
	}
	
	private static Pattern appendPattern(Pattern a, Pattern b){
		if(a == null){
			return Pattern.compile("("+b.pattern()+")");
		}
		return Pattern.compile(a.pattern()+"|("+b.pattern()+")");
	}
	
	private static Pattern buildPattern(Collection<Tokenizer> tokenizers){
		if(tokenizers.isEmpty()){
			return null;
		}
		StringBuilder builder = new StringBuilder();
		boolean first = true;
		for(Tokenizer t : tokenizers){
			if(first){
				builder.append("(").append(t.getPattern().pattern()).append(")");
				first = false;
			}else{
				builder.append("|(").append(t.getPattern().pattern()).append(")");
			}
		}
		return Pattern.compile(builder.toString());
	}

	
}
