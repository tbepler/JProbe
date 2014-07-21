package jprobe.framework.model.compiler.parser;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import jprobe.framework.model.compiler.lexer.Lexer;
import jprobe.framework.model.compiler.lexer.Token;
import jprobe.framework.model.compiler.lexer.Tokenizer;

public class GrammarImport implements Grammar<String>{
	
	private static final String ASSIGN = ":";
	private static final String PRODUCTION_SEP = "->";
	private static final String QUOTES_REGEX = "\"((?:\\\\\"|[^\"])*?)\"" ;
	private static final String SKIP_REGEX = "IGNORE:\\s*"+QUOTES_REGEX;
	private static final String TOKEN_REGEX = "([a-zA-Z]+)"+ASSIGN+"\\s*"+QUOTES_REGEX;
	private static final String PROD_REGEX = "([a-zA-Z]+)\\s*"+PRODUCTION_SEP+"\\s*([a-zA-Z]+\\s*)*";
	
	private enum Types implements Tokenizer<Types>{
		
		SKIP(SKIP_REGEX),
		TOKEN(TOKEN_REGEX),
		PRODUCTION(PROD_REGEX);

		private final Pattern m_Pattern;
		
		private Types(String regex){
			m_Pattern = Pattern.compile(regex);
		}
		
		@Override
		public Pattern getPattern() {
			return m_Pattern;
		}

		@Override
		public Token<Types> tokenize(String s) {
			return new Token<Types>(s, this);
		}
		
	}
	
	public static GrammarImport readGrammar(InputStream in) throws IOException{
		Lexer<Types> lexer = new Lexer<Types>(Types.values());
		List<Token<Types>> tokens = lexer.lex(in);
		List<String> terminators = new ArrayList<String>();
		List<Production<String>> productions = new ArrayList<Production<String>>();
		for(Token<Types> token : tokens){
			switch(token.id()){
			case PRODUCTION:
				productions.add(parseProduction(token.text()));
				break;
			case SKIP:
				//TODO
				break;
			case TOKEN:
				terminators.add(parseToken(token.text()));
				break;
			}
		}
		return new GrammarImport(terminators, productions);
	}
	
	private static String parseToken(String s){
		String[] split = s.split(ASSIGN);
		//TODO
		return split[0].trim();
	}
	
	private static Production<String> parseProduction(String s){
		String[] split = s.split(PRODUCTION_SEP);
		String lhs = split[0].trim();
		
		String[] rhs = split[1].trim().split("\\s+");
		return new ProductionImport(lhs, rhs);
	}
	
	private static final String EOF = "$";
	
	private final List<String> m_Terms;
	private final List<Production<String>> m_Prods;
	private final Map<String, Collection<Production<String>>> m_LHS;
	
	private GrammarImport(List<String> terminators, List<Production<String>> productions){
		m_Terms = terminators;
		m_Prods = productions;
		m_LHS = new HashMap<String, Collection<Production<String>>>();
		for(Production<String> p : m_Prods){
			String lhs = p.leftHandSide();
			if(m_LHS.containsKey(lhs)){
				m_LHS.get(lhs).add(p);
			}else{
				Collection<Production<String>> set = new HashSet<Production<String>>();
				set.add(p);
				m_LHS.put(lhs, set);
			}
		}
	}

	@Override
	public Iterator<Production<String>> iterator() {
		return m_Prods.iterator();
	}

	@Override
	public Collection<String> getTerminalSymbols() {
		return m_Terms;
	}

	@Override
	public boolean isTerminal(String symbol) {
		return m_Terms.contains(symbol) || this.isEOFSymbol(symbol);
	}

	@Override
	public Collection<Production<String>> getProductions(String leftHandSide) {
		if(m_LHS.containsKey(leftHandSide)){
			return Collections.unmodifiableCollection(m_LHS.get(leftHandSide));
		}
		return Collections.emptyList();
	}

	@Override
	public Collection<Production<String>> getAllProductions() {
		return Collections.unmodifiableCollection(m_Prods);
	}

	@Override
	public boolean isEOFSymbol(String symbol) {
		return EOF.equals(symbol);
	}

	@Override
	public Production<String> getStartProduction() {
		return m_Prods.get(0);
	}
	
}
