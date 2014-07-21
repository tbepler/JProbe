package jprobe.framework.model.compiler.parser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import jprobe.framework.model.compiler.HashSetHashMap;
import jprobe.framework.model.compiler.lexer.Token;

public class Parser<S> {
	
	private final Grammar<S> m_Grammar;
	private final Set<S> m_Nullable;
	private final Map<S,Set<S>> m_First;
	private final Map<S,Set<S>> m_Follow;
	private final State<S> m_Start;
	private final Map<State<S>, Map<S, Action<S>>> m_Actions;
	
	public Parser(Grammar<S> g){
		m_Grammar = g;
		m_Nullable = ParserAlgorithms.computeNullables(m_Grammar);
		m_First = ParserAlgorithms.computeFirstSets(m_Grammar, m_Nullable);
		m_Follow = ParserAlgorithms.computeFollowSets(m_Grammar, m_Nullable, m_First);
		m_Start = this.computeStartState();
		m_Actions = ParserAlgorithms.constructActionTable(this, m_Grammar);
	}
	
	private Set<S> first(List<S> symbols){
		return this.first(0, symbols);
	}
	
	private Set<S> first(int index, List<S> symbols){
		if(index >= symbols.size()){
			return new HashSet<S>();
		}
		if(!m_Nullable.contains(symbols.get(index))){
			return m_First.get(symbols.get(index));
		}
		Set<S> set = m_First.get(symbols.get(index));
		set.addAll(this.first(index+1, symbols));
		return set;
	}
	
	public Item<S> newItem(Production<S> production, List<S> lookahead){
		return new Item<S>(production, lookahead);
	}
	
	public Item<S> incrementItem(Item<S> item){
		return item.increment();
	}
	
	public State<S> newState(Set<Item<S>> items){
		return new State<S>(items);
	}
	
	public State<S> getStartState(){
		return m_Start;
	}
	
	public Collection<Production<S>> getProductions(S leftHandSide){
		return m_Grammar.getProductions(leftHandSide);
	}
	
	public Set<S> getFirst(S symbol){
		return Collections.unmodifiableSet(m_First.get(symbol));
	}
	
	public Set<S> getFirst(List<S> symbols){
		return Collections.unmodifiableSet(this.first(symbols));
	}
	
	public Set<S> getFollow(S symbol){
		return Collections.unmodifiableSet(m_Follow.get(symbol));
	}
	
	public boolean isNullable(S symbol){
		return m_Nullable.contains(symbol);
	}
	
	public Action<S> getAction(State<S> state, S symbol){
		if(m_Actions.containsKey(state)){
			return m_Actions.get(state).get(symbol);
		}
		return null;
	}
	
	public Collection<State<S>> getStates(){
		return Collections.unmodifiableSet(m_Actions.keySet());
	}
	
	public void parse(List<Token<S>> tokens){
		
	}
	
	public Action<S> newReduceAction(Production<S> reduce){
		return new Action<S>(Actions.REDUCE, reduce);
	}
	
	public Action<S> newGotoAction(State<S> next){
		return new Action<S>(Actions.GOTO, next);
	}
	
	public Action<S> newShiftAction(State<S> next){
		return new Action<S>(Actions.SHIFT, next);
	}
	
	public Action<S> newAcceptAction(){
		return new Action<S>(Actions.ACCEPT);
	}
	
	private State<S> computeStartState(){
		Set<Item<S>> start = new HashSet<Item<S>>();
		start.add(this.newItem(m_Grammar.getStartProduction(), new ArrayList<S>()));
		return ParserAlgorithms.closure(this, start);
	}

	private static <T> String iterableToString(Iterable<T> tokens){
		String s = "[";
		boolean first = true;
		for(T t : tokens){
			if(first){
				s += t;
				first = false;
			}else{
				s += ", " + t;
			}
		}
		s += "]";
		return s;
	}
	
	private static <T> Set<T> asSet(T ... objs){
		Set<T> set = new HashSet<T>();
		for(T t : objs){
			set.add(t);
		}
		return set;
	}
	
	
	
	
	
	
	
	
	
	
	
	
}
