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

	@Override
	public Iterator<Production<SabreVisitor>> iterator() {
		return Collections.unmodifiableList(m_Productions).iterator();
	}

	@Override
	public Collection<? extends Symbol<SabreVisitor>> getTerminalSymbols() {
		return Collections.unmodifiableList(m_Terminals);
	}

	@Override
	public boolean isTerminal(Symbol<SabreVisitor> symbol) {
		return m_TerminalTypes.contains(symbol.getSymbolType());
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
	public Symbol<SabreVisitor> getEOFSymbol() {
		return m_EOF;
	}

	@Override
	public List<? extends Tokenizer<SabreVisitor>> getTokenizers() {
		return Collections.unmodifiableList(m_Terminals);
	}

}
