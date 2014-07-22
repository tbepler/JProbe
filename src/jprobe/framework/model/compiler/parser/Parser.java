package jprobe.framework.model.compiler.parser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import jprobe.framework.model.compiler.grammar.Grammar;
import jprobe.framework.model.compiler.grammar.Production;
import jprobe.framework.model.compiler.grammar.Symbol;
import jprobe.framework.model.compiler.lexer.Lexer;

public class Parser<V> {
	
	private final Grammar<V> m_Grammar;
	private final Set<Class<? extends Symbol<V>>> m_Nullable;
	private final Map<Class<? extends Symbol<V>>,Set<Class<? extends Symbol<V>>>> m_First;
	private final Map<Class<? extends Symbol<V>>,Set<Class<? extends Symbol<V>>>> m_Follow;
	private final State<V> m_Start;
	private final Map<State<V>, Map<Class<? extends Symbol<V>>, Action<V>>> m_Actions;
	
	public Parser(Grammar<V> g){
		m_Grammar = g;
		m_Nullable = ParserAlgorithms.computeNullables(m_Grammar);
		m_First = ParserAlgorithms.computeFirstSets(m_Grammar, m_Nullable);
		m_Follow = ParserAlgorithms.computeFollowSets(m_Grammar, m_Nullable, m_First);
		m_Start = this.computeStartState();
		m_Actions = ParserAlgorithms.constructActionTable(this, m_Grammar);
	}
	
	private Set<Class<? extends Symbol<V>>> first(List<Class<? extends Symbol<V>>> symbols){
		return this.first(0, symbols);
	}
	
	private Set<Class<? extends Symbol<V>>> first(int index, List<Class<? extends Symbol<V>>> symbols){
		if(index >= symbols.size()){
			return new HashSet<Class<? extends Symbol<V>>>();
		}
		if(!m_Nullable.contains(symbols.get(index))){
			return m_First.get(symbols.get(index));
		}
		Set<Class<? extends Symbol<V>>> set = m_First.get(symbols.get(index));
		set.addAll(this.first(index+1, symbols));
		return set;
	}
	
	public Symbol<V> parse(List<Symbol<V>> tokens){
		Deque<State<V>> states = new LinkedList<State<V>>();
		states.push(m_Start);
		Deque<Symbol<V>> symbols = new LinkedList<Symbol<V>>();
		Queue<Symbol<V>> remaining = new LinkedList<Symbol<V>>(tokens);
		while(true){
			State<V> state = states.peek();
			Symbol<V> lookahead = remaining.peek();
			Action<V> action = this.getAction(state, lookahead);
			switch(action.id()){
			case ACCEPT:
				return symbols.peek();
			case GOTO:
				states.push(action.nextState());
				break;
			case REDUCE:
				this.reduce(symbols, action.production());
				states.pop();
				break;
			case SHIFT:
				symbols.push(remaining.poll());
				states.push(action.nextState());
				break;
			default:
				throw new RuntimeException("Unknown action: "+action.id());
			}
		}
	}
	
	public Symbol<V> parse(Lexer<V> lexer){
		Deque<State<V>> states = new LinkedList<State<V>>();
		states.push(m_Start);
		Deque<Symbol<V>> symbols = new LinkedList<Symbol<V>>();
		Symbol<V> lookahead;
		while(lexer.hasNext()){
			lookahead = lexer.nextToken();
			State<V> state = states.peek();
			Action<V> action = this.getAction(state, lookahead);
			if(action == null){
				//ERROR
				throw new RuntimeException("Error on token: "+lookahead);
			}
			switch(action.id()){
			case ACCEPT:
				return symbols.peek();
			case GOTO:
				states.push(action.nextState());
				break;
			case REDUCE:
				this.reduce(symbols, action.production());
				states.pop();
				break;
			case SHIFT:
				symbols.push(lookahead);
				states.push(action.nextState());
				break;
			default:
				throw new RuntimeException("Unknown action: "+action.id());
			}
		}
		throw new RuntimeException("All symbols exhausted and no accept action reached.");
	}
	
	
	private void reduce(Deque<Symbol<V>> stack, Production<V> rule){
		List<Symbol<V>> lhs = new ArrayList<Symbol<V>>();
		int len = rule.rightHandSide().size();
		for(int i=0; i<len; ++i){
			lhs.add(0, stack.pop());
		}
		stack.push(rule.reduce(lhs));
	}
	
	public Action<V> newReduceAction(Production<V> reduce){
		return new Action<V>(Actions.REDUCE, reduce);
	}
	
	public Action<V> newGotoAction(State<V> next){
		return new Action<V>(Actions.GOTO, next);
	}
	
	public Action<V> newShiftAction(State<V> next){
		return new Action<V>(Actions.SHIFT, next);
	}
	
	public Action<V> newAcceptAction(){
		return new Action<V>(Actions.ACCEPT);
	}
	
	public Item<V> newItem(Production<V> production, List<Class<? extends Symbol<V>>> lookahead){
		return new Item<V>(production, lookahead);
	}
	
	public Item<V> incrementItem(Item<V> item){
		return item.increment();
	}
	
	public State<V> newState(Set<Item<V>> items){
		return new State<V>(items);
	}
	
	public State<V> getStartState(){
		return m_Start;
	}
	
	public Collection<Production<V>> getProductions(Class<? extends Symbol<V>> leftHandSide){
		return m_Grammar.getProductions(leftHandSide);
	}
	
	public Set<Class<? extends Symbol<V>>> getFirst(Class<? extends Symbol<V>> symbolType){
		return Collections.unmodifiableSet(m_First.get(symbolType));
	}
	
	public Set<Class<? extends Symbol<V>>> getFirst(List<Class<? extends Symbol<V>>> symbolTypes){
		return Collections.unmodifiableSet(this.first(symbolTypes));
	}
	
	public Set<Class<? extends Symbol<V>>> getFollow(Class<? extends Symbol<V>> symbolType){
		return Collections.unmodifiableSet(m_Follow.get(symbolType));
	}
	
	public boolean isNullable(Class<? extends Symbol<V>> symbolType){
		return m_Nullable.contains(symbolType);
	}
	
	public Action<V> getAction(State<V> state, Symbol<V> symbol){
		return this.getAction(state, symbol.getSymbolType());
	}
	
	public Action<V> getAction(State<V> state, Class<? extends Symbol<V>> symbolType){
		if(m_Actions.containsKey(state)){
			return m_Actions.get(state).get(symbolType);
		}
		return null;
	}
	
	public Collection<State<V>> getStates(){
		return Collections.unmodifiableSet(m_Actions.keySet());
	}
	
	private State<V> computeStartState(){
		Set<Item<V>> start = new HashSet<Item<V>>();
		start.add(this.newItem(m_Grammar.getStartProduction(), new ArrayList<Class<? extends Symbol<V>>>()));
		return ParserAlgorithms.closure(this, start);
	}

	
	
	
	
	
	
	
	
	
	
	
	
}
