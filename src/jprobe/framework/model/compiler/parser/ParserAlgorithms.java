package jprobe.framework.model.compiler.parser;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import jprobe.framework.model.compiler.HashSetHashMap;
import jprobe.framework.model.compiler.grammar.Grammar;
import jprobe.framework.model.compiler.grammar.Production;

public class ParserAlgorithms {
	
	public static <S> Map<State<S>,Map<S,Action<S>>> constructActionTable(Parser<S> parser, Grammar<S> g){
		Set<State<S>> states = initializeStatesSet(parser);
		Map<State<S>, Map<S,Action<S>>> actionTable = new HashMap<State<S>, Map<S, Action<S>>>();
		
		Queue<State<S>> stateQ = new LinkedList<State<S>>();
		stateQ.addAll(states);
		
		while(!stateQ.isEmpty()){
			State<S> cur = stateQ.poll();
			
			for(Item<S> item : cur){
				
				if(item.hasNext()){
					S next = item.next();
					if(g.isEOFSymbol(next)){
						//accept action
						Action<S> a = parser.newAcceptAction();
						if(containsKeys(actionTable, cur, next)){
							System.err.println("Warning: grammar "+g+"  contains ambiguous actions.");
							System.err.println("Current: "+actionTable.get(cur).get(next));
							System.err.println("Replacement: "+a);
						}
						actionTable = add(actionTable, cur, next, a);
					}else{
						State<S> descendent = gotoo(parser, cur, next);
						if(states.add(descendent)){
							stateQ.add(descendent);
							Action<S> a;
							if(g.isTerminal(next)){
								a = parser.newShiftAction(descendent);
							}else{
								a = parser.newGotoAction(descendent);
							}
							if(containsKeys(actionTable, cur, next)){
								System.err.println("Warning: grammar "+g+"  contains ambiguous actions.");
								System.err.println("Current: "+actionTable.get(cur).get(next));
								System.err.println("Replacement: "+a);
							}
							actionTable = add(actionTable, cur, next, a);
						}
					}
				}else{
					//reduce action
					Action<S> action = parser.newReduceAction(item.getProduction());
					if(item.lookaheadLength() > 0){
						S lookahead = item.lookahead(0);
						if(containsKeys(actionTable, cur, lookahead)){
							System.err.println("Warning: grammar "+g+"  contains ambiguous actions.");
							System.err.println("Current: "+actionTable.get(cur).get(lookahead));
							System.err.println("Replacement: "+action);
						}
						add(actionTable, cur, lookahead, action);
					}else{
						for(S terminal : g.getTerminalSymbols()){
							if(containsKeys(actionTable, cur, terminal)){
								System.err.println("Warning: grammar "+g+"  contains ambiguous actions.");
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
	
	private static <S> Set<State<S>> initializeStatesSet(Parser<S> parser){
		Set<State<S>> states = new HashSet<State<S>>();
		states.add(parser.getStartState());
		return states;
	}
	
	public static <S> State<S> gotoo(Parser<S> parser, State<S> state, S symbol){
		Set<Item<S>> shifted = new HashSet<Item<S>>();
		for(Item<S> item : state){
			if(item.hasNext() && symbol.equals(item.next())){
				shifted.add(parser.incrementItem(item));
			}
		}
		return closure(parser, shifted);
	}
	
	public static <S> State<S> closure(Parser<S> parser, Set<Item<S>> items){
		Queue<Item<S>> itemQ = new LinkedList<Item<S>>(items);
		while(!itemQ.isEmpty()){
			Item<S> item = itemQ.poll();
			if(item.hasNext()){
				S next = item.next();
				List<S> symbols = item.beta();
				symbols.addAll(item.lookahead());
				Set<S> firstSet = parser.getFirst(symbols);
				for(Production<S> p : parser.getProductions(next)){
					for(S symbol : firstSet){
						@SuppressWarnings("unchecked")
						Item<S> newItem = parser.newItem(p, Arrays.asList(symbol));
						if(items.add(newItem)){
							itemQ.add(newItem);
						}
					}
				}
			}
		}
		return parser.newState(items);
	}
	
	public static <S> Map<S,Set<S>> computeFollowSets(Grammar<S> g, Set<S> nullables, Map<S,Set<S>> firstSets){
		Map<S,Set<S>> followSets = new HashSetHashMap<S,S>();
		boolean changed;
		S lhs;
		S[] rhs;
		Set<S> follow;
		do{
			changed = false;
			for(Production<S> p : g){
				lhs = p.leftHandSide();
				rhs = p.rightHandSide();
				for(int i=0; i<rhs.length-1; ++i){
					if(!g.isTerminal(rhs[i])){
						follow = followSets.get(rhs[i]);
						for(int j=i+1; j<rhs.length; ++j){
							changed = follow.addAll(firstSets.get(rhs[j])) || changed;
						}
						followSets.put(rhs[i], follow);
					}
				}
				for(int i=rhs.length-1; i>=0; --i){
					if(!g.isTerminal(rhs[i]) && nullable(rhs, i+1, rhs.length, nullables)){
						follow = followSets.get(rhs[i]);
						changed = follow.addAll(followSets.get(lhs)) || changed;
						followSets.put(rhs[i], follow);
					}
				}
			}
		}while(changed);
		return followSets;
	}
	
	public static <S> Map<S,Set<S>> computeFirstSets(Grammar<S> g, Set<S> nullables){
		Map<S,Set<S>> firstSets = new HashSetHashMap<S,S>();
		for(S terminal : g.getTerminalSymbols()){
			Set<S> set = firstSets.get(terminal);
			set.add(terminal);
			firstSets.put(terminal, set);
		}
		boolean changed;
		S lhs;
		S[] rhs;
		do{
			changed = false;
			for(Production<S> p : g){
				lhs = p.leftHandSide();
				rhs = p.rightHandSide();
				if(rhs.length > 0){
					Set<S> first = firstSets.get(lhs);
					changed = first.addAll(firstSets.get(rhs[0])) || changed;
					for(int i=1; i<rhs.length; ++i){
						if(nullable(rhs, 0, i, nullables)){
							changed = first.addAll(firstSets.get(rhs[i])) || changed;
						}
					}
					firstSets.put(lhs, first);
				}
			}
		}while(changed);
		return firstSets;
	}
	
	public static <S> Set<S> computeNullables(Grammar<S> g){
		Set<S> nullables = new HashSet<S>();
		boolean changed;
		S lhs;
		S[] rhs;
		do{
			changed = false;
			for(Production<S> p : g){
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
	
}
