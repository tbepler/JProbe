package jprobe.framework.model.compiler.parser;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import jprobe.framework.model.compiler.Element;
import jprobe.framework.model.compiler.HashSetHashMap;
import jprobe.framework.model.compiler.lexer.Token;

public class Parser<S> {
	
	private final Grammar<S> m_Grammar;
	private final Set<S> m_Nullable;
	private final Map<S,Set<S>> m_First;
	private final Map<S,Set<S>> m_Follow;
	private final Map<State<S>, Map<S, Action<S>>> m_Actions;
	
	public Parser(Grammar<S> g){
		m_Grammar = g;
		m_Nullable = this.computeNullables();
		m_First = this.computeFirstSets();
		m_Follow = this.computeFollowSets();
		m_Actions = this.constructActionTable();
	}
	
	private Set<S> computeNullables(){
		Set<S> nullables = new HashSet<S>();
		boolean changed;
		S lhs;
		S[] rhs;
		do{
			changed = false;
			for(Production<S> p : m_Grammar){
				lhs = p.leftHandSide();
				if(nullables.contains(lhs)){
					continue;
				}
				rhs = p.rightHandSide();
				if(nullable(rhs,nullables)){
					changed = nullables.add(lhs) || changed;
				}
			}
		}while(changed);
		return nullables;
	}
	
	private Map<S,Set<S>> computeFirstSets(){
		Map<S,Set<S>> firstSets = new HashSetHashMap<S,S>();
		for(S terminal : m_Grammar.getTerminalSymbols()){
			Set<S> set = firstSets.get(terminal);
			set.add(terminal);
			firstSets.put(terminal, set);
		}
		boolean changed;
		S lhs;
		S[] rhs;
		do{
			changed = false;
			for(Production<S> p : m_Grammar){
				lhs = p.leftHandSide();
				rhs = p.rightHandSide();
				if(rhs.length > 0){
					Set<S> first = firstSets.get(lhs);
					changed = first.addAll(firstSets.get(rhs[0])) || changed;
					for(int i=1; i<rhs.length; ++i){
						if(nullable(rhs, 0, i, m_Nullable)){
							changed = first.addAll(firstSets.get(rhs[i])) || changed;
						}
					}
					firstSets.put(lhs, first);
				}
			}
		}while(changed);
		return firstSets;
	}
	
	private Map<S,Set<S>> computeFollowSets(){
		Map<S,Set<S>> followSets = new HashSetHashMap<S,S>();
		boolean changed;
		S lhs;
		S[] rhs;
		Set<S> follow;
		do{
			changed = false;
			for(Production<S> p : m_Grammar){
				lhs = p.leftHandSide();
				rhs = p.rightHandSide();
				for(int i=0; i<rhs.length-1; ++i){
					if(!m_Grammar.isTerminal(rhs[i])){
						follow = followSets.get(rhs[i]);
						for(int j=i+1; j<rhs.length; ++j){
							changed = follow.addAll(m_First.get(rhs[j])) || changed;
						}
						followSets.put(rhs[i], follow);
					}
				}
				for(int i=rhs.length-1; i>=0; --i){
					if(!m_Grammar.isTerminal(rhs[i]) && nullable(rhs, i+1, rhs.length, m_Nullable)){
						follow = followSets.get(rhs[i]);
						changed = follow.addAll(followSets.get(lhs)) || changed;
						followSets.put(rhs[i], follow);
					}
				}
			}
		}while(changed);
		return followSets;
	}
	
	private static <S> boolean nullable(S[] symbols, int start, int stop, Set<S> nullables){
		for(int i=start; i<stop; ++i){
			if(!nullables.contains(symbols[i])){
				return false;
			}
		}
		return true;
	}
	
	private static <S> boolean nullable(S[] symbols, Set<S> nullables){
		for(S symbol : symbols){
			if(!nullables.contains(symbol)){
				return false;
			}
		}
		return true;
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
	
	public Set<S> getFirst(S symbol){
		return Collections.unmodifiableSet(m_First.get(symbol));
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
	
	public void parse(List<Token> tokens){
		
	}
	
	private Map<State<S>,Map<S,Action<S>>> constructActionTable(){
		Set<State<S>> states = this.initializeStatesSet();
		Map<State<S>, Map<S,Action<S>>> actionTable = new HashMap<State<S>, Map<S, Action<S>>>();
		
		Queue<State<S>> stateQ = new LinkedList<State<S>>();
		stateQ.addAll(states);
		
		while(!stateQ.isEmpty()){
			State<S> cur = stateQ.poll();
			
			for(Item<S> item : cur){
				
				if(item.hasNext()){
					S next = item.next();
					if(m_Grammar.isEOFSymbol(next)){
						//accept action
						Action<S> a = this.getAcceptAction();
						if(containsKeys(actionTable, cur, next)){
							System.err.println("Warning: grammar "+m_Grammar+"  contains ambiguous actions.");
							System.err.println("Current: "+actionTable.get(cur).get(next));
							System.err.println("Replacement: "+a);
						}
						actionTable = add(actionTable, cur, next, a);
					}else{
						State<S> descendent = this.gotoo(cur, next);
						if(states.add(descendent)){
							stateQ.add(descendent);
							Action<S> a;
							if(m_Grammar.isTerminal(next)){
								a = this.getShiftAction(descendent);
							}else{
								a = this.getGotoAction(descendent);
							}
							if(containsKeys(actionTable, cur, next)){
								System.err.println("Warning: grammar "+m_Grammar+"  contains ambiguous actions.");
								System.err.println("Current: "+actionTable.get(cur).get(next));
								System.err.println("Replacement: "+a);
							}
							actionTable = add(actionTable, cur, next, a);
						}
					}
				}else{
					//reduce action
					Action<S> action = this.getReduceAction(item.getProduction());
					if(item.lookaheadLength() > 0){
						S lookahead = item.lookahead(0);
						if(containsKeys(actionTable, cur, lookahead)){
							System.err.println("Warning: grammar "+m_Grammar+"  contains ambiguous actions.");
							System.err.println("Current: "+actionTable.get(cur).get(lookahead));
							System.err.println("Replacement: "+action);
						}
						add(actionTable, cur, lookahead, action);
					}else{
						for(S terminal : m_Grammar.getTerminalSymbols()){
							if(containsKeys(actionTable, cur, terminal)){
								System.err.println("Warning: grammar "+m_Grammar+"  contains ambiguous actions.");
								System.err.println("Current: "+actionTable.get(cur).get(terminal));
								System.err.println("Replacement: "+action);
							}
							add(actionTable, cur, terminal, action);
						}
					}
				}
			}
		}
		return actionTable;
	}
	
	private Action<S> getReduceAction(Production<S> reduce){
		return new Action<S>(Actions.REDUCE, reduce);
	}
	
	private Action<S> getGotoAction(State<S> next){
		return new Action<S>(Actions.GOTO, next);
	}
	
	private Action<S> getShiftAction(State<S> next){
		return new Action<S>(Actions.SHIFT, next);
	}
	
	private Action<S> getAcceptAction(){
		return new Action<S>(Actions.ACCEPT);
	}
	
	private static <K1,K2,V> boolean containsKeys(Map<K1,Map<K2,V>> table, K1 key1, K2 key2){
		if(table.containsKey(key1)){
			return table.get(key1).containsKey(key2);
		}
		return false;
	}
	
	private static <K1,K2,V> Map<K1,Map<K2,V>> add(Map<K1,Map<K2,V>> table, K1 key1, K2 key2, V value){
		if(table.containsKey(key1)){
			table.get(key1).put(key2, value);
		}else{
			Map<K2,V> map = new HashMap<K2,V>();
			map.put(key2, value);
			table.put(key1, map);
		}
		return table;
	}
	
	private Set<State<S>> initializeStatesSet(){
		Set<State<S>> states = new HashSet<State<S>>();
		Set<Item<S>> start = new HashSet<Item<S>>();
		start.add(Item.forProduction(m_Grammar.getEOFStartProduction()));
		states.add(this.closure(start));
		return states;
	}
	
	private State<S> gotoo(State<S> state, S symbol){
		Set<Item<S>> shifted = new HashSet<Item<S>>();
		for(Item<S> item : state){
			if(item.hasNext() && symbol.equals(item.next())){
				shifted.add(item.increment());
			}
		}
		return this.closure(shifted);
	}
	
	private State<S> closure(State<S> state){
		return this.closure(state.asSet());
	}
	
	private State<S> closure(Set<Item<S>> items){
		Queue<Item<S>> itemQ = new LinkedList<Item<S>>(items);
		while(!itemQ.isEmpty()){
			Item<S> item = itemQ.poll();
			if(item.hasNext()){
				S next = item.next();
				List<S> symbols = item.beta();
				symbols.addAll(item.lookahead());
				Set<S> firstSet = this.first(symbols);
				for(Production<S> p : m_Grammar.getProductions(next)){
					for(S symbol : firstSet){
						Item<S> newItem = Item.forProduction(p, symbol);
						if(items.add(newItem)){
							itemQ.add(newItem);
						}
					}
				}
			}
		}
		return State.forSet(items);
	}
	
	private static String generateBadParseMessage(List<Token> tokens, Deque<Element> stack){
		String msg = "Bad parse. Input tokens: "
			+ iterableToString(tokens)
			+ " resulted in invalid parse stack: "
			+ iterableToString(stack);
		return msg;
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
