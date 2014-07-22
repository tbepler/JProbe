package jprobe.framework.model.compiler.grammar.implementation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import jprobe.framework.model.compiler.grammar.Grammar;
import jprobe.framework.model.compiler.grammar.Production;
import jprobe.framework.model.compiler.grammar.Symbol;
import jprobe.framework.model.compiler.grammar.implementation.symbols.Terminal;
import jprobe.framework.model.compiler.lexer.Tokenizer;

public class SabreGrammar implements Grammar<SabreVisitor>{
	
	private final Symbol<SabreVisitor> m_EOF;
	private final Production<SabreVisitor> m_Start;
	private final List<Terminal> m_Terminals;
	private final Set<Class<? extends Symbol<SabreVisitor>>> m_TerminalTypes;
	private final Pattern m_TokenPattern;
	private final List<Pattern> m_Patterns;
	private final List<Production<SabreVisitor>> m_Productions;
	private final Map<Class<? extends Symbol<SabreVisitor>>, Collection<Production<SabreVisitor>>> m_LHS;
	
	public SabreGrammar(){
		SabreSymbolsFactory fac = new SabreSymbolsFactory();
		m_EOF = fac.newEOFSymbol();
		m_Start = fac.newStartProduction();
		m_Terminals = fac.newTerminals();
		m_TerminalTypes = new HashSet<Class<? extends Symbol<SabreVisitor>>>();
		for(Symbol<SabreVisitor> s : m_Terminals){
			m_TerminalTypes.add(s.getSymbolType());
		}
		m_TokenPattern = this.generateTokenPattern();
		m_Patterns = new ArrayList<Pattern>(m_Terminals.size());
		for(Tokenizer<?> t : m_Terminals){
			m_Patterns.add(Pattern.compile(t.getRegex()));
		}
		m_Productions = fac.newProductions();
		m_LHS = new HashMap<Class<? extends Symbol<SabreVisitor>>, Collection<Production<SabreVisitor>>>();
		for(Production<SabreVisitor> p : m_Productions){
			Class<? extends Symbol<SabreVisitor>> lhs = p.leftHandSide();
			if(m_LHS.containsKey(lhs)){
				m_LHS.get(lhs).add(p);
			}else{
				Collection<Production<SabreVisitor>> list = new ArrayList<Production<SabreVisitor>>();
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
	public Iterator<Production<SabreVisitor>> iterator() {
		return Collections.unmodifiableList(m_Productions).iterator();
	}

	@Override
	public Collection<Class<? extends Symbol<SabreVisitor>>> getTerminalSymbolTypes() {
		return Collections.unmodifiableSet(m_TerminalTypes);
	}

	@Override
	public boolean isTerminal(Symbol<SabreVisitor> symbol) {
		return m_TerminalTypes.contains(symbol.getSymbolType());
	}
	
	@Override
	public boolean isTerminal(Class<? extends Symbol<SabreVisitor>> symbolType){
		return m_TerminalTypes.contains(symbolType);
	}

	@Override
	public Collection<Production<SabreVisitor>> getProductions(Class<? extends Symbol<SabreVisitor>> symbolType) {
		return Collections.unmodifiableCollection(m_LHS.get(symbolType));
	}

	@Override
	public Collection<Production<SabreVisitor>> getAllProductions() {
		return Collections.unmodifiableList(m_Productions);
	}

	@Override
	public Production<SabreVisitor> getStartProduction() {
		return m_Start;
	}

	@Override
	public boolean isEOFSymbol(Symbol<SabreVisitor> symbol) {
		return m_EOF.equals(symbol);
	}
	
	@Override
	public boolean isEOFSymbol(Class<? extends Symbol<SabreVisitor>> symbolType) {
		return m_EOF.getSymbolType().equals(symbolType);
	}

	@Override
	public Symbol<SabreVisitor> getEOFSymbol() {
		return m_EOF;
	}

	@Override
	public boolean isStartSymbol(Symbol<SabreVisitor> symbol) {
		return symbol.getSymbolType().equals(m_Start.leftHandSide());
	}

	@Override
	public Pattern getTokenRegex() {
		return m_TokenPattern;
	}

	@Override
	public Symbol<SabreVisitor> tokenize(String s) {
		for(int i=0; i<m_Terminals.size(); ++i){
			if(m_Patterns.get(i).matcher(s).matches()){
				return m_Terminals.get(i).tokenize(s);
			}
		}
		throw new RuntimeException("String \""+s+"\" does not match any known token patterns.");
	}



}
