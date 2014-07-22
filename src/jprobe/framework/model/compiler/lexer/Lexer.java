package jprobe.framework.model.compiler.lexer;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jprobe.framework.model.compiler.grammar.Grammar;
import jprobe.framework.model.compiler.grammar.Symbol;

public class Lexer<T> {
	
	private final List<? extends Tokenizer<T>> m_Tokenizers;
	private final List<Pattern> m_Patterns;
	private final Grammar<T> m_Grammar;
	private Pattern m_TokenPattern;
	
	public Lexer(Grammar<T> g){
		m_Grammar = g;
		m_Tokenizers = m_Grammar.getTokenizers();
		m_Patterns = new ArrayList<Pattern>(m_Tokenizers.size());
		for(Tokenizer<T> t : m_Tokenizers){
			m_Patterns.add(Pattern.compile(t.getRegex()));
		}
		m_TokenPattern = Lexer.buildPattern(m_Tokenizers);
	}
	
	public List<Symbol<T>> lex(String s){
		//do this using matcher rather than wrapping the string
		//into a scanner, because the matcher is significantly
		//faster
		List<Symbol<T>> tokens = new ArrayList<Symbol<T>>();
		Matcher tokenMatcher = this.tokenPattern().matcher(s);
		//System.out.println(this.tokenPattern().pattern());
		while(tokenMatcher.find()){
			Symbol<T> token = this.tokenize(tokenMatcher.group());
			//if token is null, then it is an ignored type
			if(token != null){
				tokens.add(token);
			}
		}
		tokens.add(m_Grammar.getEOFSymbol());
		return tokens;
		
	}
	
	public List<Symbol<T>> lex(InputStream in){
		return this.lex(new Scanner(in));
	}
	
	public List<Symbol<T>> lex(Scanner s){
		List<Symbol<T>> tokens = new ArrayList<Symbol<T>>();
		String match;
		while((match = s.findWithinHorizon(this.tokenPattern(), 0)) != null){
			Symbol<T> token = this.tokenize(match);
			//if token is null, then it is an ignored type
			if(token != null){
				tokens.add(token);
			}
		}
		tokens.add(m_Grammar.getEOFSymbol());
		return tokens;
	}
	
	private Symbol<T> tokenize(String s){
		for(int i=0; i<m_Patterns.size(); ++i){
			if(m_Patterns.get(i).matcher(s).matches()){
				return m_Tokenizers.get(i).tokenize(s);
			}
		}
		throw new LexingException("String \""+s+"\" does not match any known token patterns.");
	}
	
	private Pattern tokenPattern(){
		return m_TokenPattern;
	}
	
	private static <U> Pattern buildPattern(Collection<? extends Tokenizer<U>> tokenizers){
		if(tokenizers.isEmpty()){
			return null;
		}
		StringBuilder builder = new StringBuilder();
		boolean first = true;
		for(Tokenizer<?> t : tokenizers){
			if(first){
				builder.append("(").append(t.getRegex()).append(")");
				first = false;
			}else{
				builder.append("|(").append(t.getRegex()).append(")");
			}
		}
		return Pattern.compile(builder.toString());
	}

	
}
