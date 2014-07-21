package jprobe.framework.model.compiler.lexer;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jprobe.framework.model.compiler.grammar.Tokenizer;

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
	
	public List<Token<T>> lex(String s){
		//do this using matcher rather than wrapping the string
		//into a scanner, because the matcher is significantly
		//faster
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
	
	public List<Token<T>> lex(InputStream in){
		return this.lex(new Scanner(in));
	}
	
	public List<Token<T>> lex(Scanner s){
		List<Token<T>> tokens = new ArrayList<Token<T>>();
		String match;
		while((match = s.findWithinHorizon(this.tokenPattern(), 0)) != null){
			Token<T> token = this.tokenize(match);
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
