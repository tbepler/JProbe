package language.implementation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import language.compiler.grammar.Grammar;
import language.compiler.grammar.Production;
import language.compiler.grammar.Token;
import language.compiler.lexer.Tokenizer;
import language.implementation.symbols.Terminal;

public class SabreGrammar implements Grammar<Visitor>{
	
	private final Terminal m_EOF;
	private final Production<Visitor> m_Start;
	private final List<Terminal> m_Terminals;
	private final Map<Class<? extends Token<Visitor>>, Terminal> m_TerminalTypes;
	private final Pattern m_TokenPattern;
	private final List<Pattern> m_Patterns;
	private final List<Production<Visitor>> m_Productions;
	private final Map<Class<? extends Token<Visitor>>, Collection<Production<Visitor>>> m_LHS;
	
	public SabreGrammar(){
		SabreTokenFactory fac = new SabreTokenFactory();
		m_EOF = fac.newEOFToken();
		m_Start = fac.newStartProduction();
		m_Terminals = fac.newTerminals();
		m_TerminalTypes = new HashMap<Class<? extends Token<Visitor>>, Terminal>();
		for(Terminal s : m_Terminals){
			m_TerminalTypes.put(s.getSymbolType(), s);
		}
		m_TerminalTypes.put(m_EOF.getSymbolType(), m_EOF);
		m_TokenPattern = this.generateTokenPattern();
		m_Patterns = new ArrayList<Pattern>(m_Terminals.size());
		for(Tokenizer<?> t : m_Terminals){
			m_Patterns.add(Pattern.compile(t.getRegex()));
		}
		m_Productions = fac.newProductions();
		m_LHS = new HashMap<Class<? extends Token<Visitor>>, Collection<Production<Visitor>>>();
		for(Production<Visitor> p : m_Productions){
			Class<? extends Token<Visitor>> lhs = p.leftHandSide();
			if(m_LHS.containsKey(lhs)){
				m_LHS.get(lhs).add(p);
			}else{
				Collection<Production<Visitor>> list = new ArrayList<Production<Visitor>>();
				list.add(p);
				m_LHS.put(lhs, list);
			}
		}
	}
	
	private Pattern generateTokenPattern(){
		StringBuilder builder = new StringBuilder();
		boolean first = true;
		for(Tokenizer<?> t : m_Terminals){
			if(first){
				builder.append("(").append(t.getRegex()).append(")");
				first = false;
			}else{
				builder.append("|(").append(t.getRegex()).append(")");
			}
		}
		return Pattern.compile(builder.toString());
	}

	@Override
	public Iterator<Production<Visitor>> iterator() {
		return Collections.unmodifiableList(m_Productions).iterator();
	}

	@Override
	public Collection<Class<? extends Token<Visitor>>> getTerminalSymbolTypes() {
		return Collections.unmodifiableSet(m_TerminalTypes.keySet());
	}

	@Override
	public boolean isTerminal(Token<Visitor> symbol) {
		return m_TerminalTypes.containsKey(symbol.getSymbolType());
	}
	
	@Override
	public boolean isTerminal(Class<? extends Token<Visitor>> symbolType){
		return m_TerminalTypes.containsKey(symbolType);
	}

	@Override
	public Collection<Production<Visitor>> getProductions(Class<? extends Token<Visitor>> symbolType) {
		if(!m_LHS.containsKey(symbolType)){
			return Collections.emptySet();
		}
		return Collections.unmodifiableCollection(m_LHS.get(symbolType));
	}

	@Override
	public Collection<Production<Visitor>> getAllProductions() {
		return Collections.unmodifiableList(m_Productions);
	}

	@Override
	public Production<Visitor> getStartProduction() {
		return m_Start;
	}

	@Override
	public boolean isEOFSymbol(Token<Visitor> symbol) {
		return m_EOF.equals(symbol);
	}
	
	@Override
	public boolean isEOFSymbol(Class<? extends Token<Visitor>> symbolType) {
		return m_EOF.getSymbolType().equals(symbolType);
	}

	@Override
	public Token<Visitor> getEOFSymbol() {
		return m_EOF;
	}

	@Override
	public boolean isStartSymbol(Token<Visitor> symbol) {
		return symbol.getSymbolType().equals(m_Start.leftHandSide());
	}

	@Override
	public Pattern getTokenRegex() {
		return m_TokenPattern;
	}

	@Override
	public Token<Visitor> tokenize(String s) {
		for(int i=0; i<m_Terminals.size(); ++i){
			if(m_Patterns.get(i).matcher(s).matches()){
				return m_Terminals.get(i).tokenize(s);
			}
		}
		throw new RuntimeException("String \""+s+"\" does not match any known token patterns.");
	}

	@Override
	public int getPrecedence(Class<? extends Token<Visitor>> symbolType) {
		return m_TerminalTypes.get(symbolType).getPriority();
	}



}
