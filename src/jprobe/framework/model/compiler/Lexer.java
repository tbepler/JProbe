package jprobe.framework.model.compiler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer<T> {
	
	private final Collection<Tokenizer<T>> m_Tokenizers;
	private Pattern m_TokenPattern;
	
	public Lexer(){
		m_Tokenizers = new ArrayList<Tokenizer<T>>();
		m_TokenPattern = null;
	}
	
	public Lexer(Tokenizer<T> ... tokenizers){
		m_Tokenizers = new ArrayList<Tokenizer<T>>(Arrays.asList(tokenizers));
		m_TokenPattern = buildPattern(m_Tokenizers);
	}
	
	public Lexer(Collection<Tokenizer<T>> tokenizers){
		m_Tokenizers = new ArrayList<Tokenizer<T>>(tokenizers);
		m_TokenPattern = buildPattern(tokenizers);
	}
	
	public void addTokenizer(Tokenizer<T> t){
		m_Tokenizers.add(t);
		m_TokenPattern = appendPattern(m_TokenPattern, t.getPattern());
	}
	
	public void removeTokenizer(Tokenizer<T> t){
		m_Tokenizers.remove(t);
		m_TokenPattern = buildPattern(m_Tokenizers);
	}
	
	public List<Token<T>> lex(String s){
		List<Token<T>> tokens = new ArrayList<Token<T>>();
		Matcher tokenMatcher = this.tokenPattern().matcher(s);
		//System.out.println(this.tokenPattern().pattern());
		while(tokenMatcher.find()){
			Token<T> token = this.tokenize(tokenMatcher.group());
			//if token is null, then it is an ignored type
			if(token != null){
				tokens.add(token);
			}
		}
		return tokens;
	}
	
	private Token<T> tokenize(String s){
		for(Tokenizer<T> t : m_Tokenizers){
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
	
	private static <U> Pattern buildPattern(Collection<Tokenizer<U>> tokenizers){
		if(tokenizers.isEmpty()){
			return null;
		}
		StringBuilder builder = new StringBuilder();
		boolean first = true;
		for(Tokenizer<?> t : tokenizers){
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
