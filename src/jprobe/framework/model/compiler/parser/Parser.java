package jprobe.framework.model.compiler.parser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import jprobe.framework.model.compiler.grammar.Production;
import jprobe.framework.model.compiler.grammar.Symbol;
import jprobe.framework.model.compiler.lexer.Lexer;

public class Parser<V> {
	
	private final Map<State<V>, Map<Class<? extends Symbol<V>>, Action<V>>> m_Actions;
	private final State<V> m_Start;
	
	public Parser(Map<State<V>, Map<Class<? extends Symbol<V>>, Action<V>>> actionTable, State<V> startState){
		m_Actions = actionTable;
		m_Start = startState;
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
			if(action == null){
				//ERROR //TODO
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
				//ERROR //TODO
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


	
	
	
	
	
	
	
	
	
	
	
	
}
