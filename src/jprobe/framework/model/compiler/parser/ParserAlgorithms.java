package jprobe.framework.model.compiler.parser;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import jprobe.framework.model.compiler.HashSetHashMap;
import jprobe.framework.model.compiler.ListUtil;
import jprobe.framework.model.compiler.grammar.Grammar;
import jprobe.framework.model.compiler.grammar.Production;
import jprobe.framework.model.compiler.grammar.Symbol;

public class ParserAlgorithms {
	
	public static <V> Map<State<V>,Map<Class<? extends Symbol<V>>,Action<V>>> constructActionTable(Parser<V> parser, Grammar<V> g){
		Set<State<V>> states = initializeStatesSet(parser);
		Map<State<V>, Map<Class<? extends Symbol<V>>,Action<V>>> actionTable = new HashMap<State<V>, Map<Class<? extends Symbol<V>>, Action<V>>>();
		
		Queue<State<V>> stateQ = new LinkedList<State<V>>();
		stateQ.addAll(states);
		
		while(!stateQ.isEmpty()){
			State<V> cur = stateQ.poll();
			
			for(Item<V> item : cur){
				
				if(item.hasNext()){
					Class<? extends Symbol<V>> next = item.next();
					if(g.isEOFSymbol(next)){
						//accept action
						Action<V> a = parser.newAcceptAction();
						if(containsKeys(actionTable, cur, next)){
							System.err.println("Warning: grammar "+g+"  contains ambiguous actions.");
							System.err.println("Current: "+actionTable.get(cur).get(next));
							System.err.println("Replacement: "+a);
						}
						actionTable = add(actionTable, cur, next, a);
					}else{
						State<V> descendent = gotoo(parser, cur, next);
						if(states.add(descendent)){
							stateQ.add(descendent);
							Action<V> a;
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
					Action<V> action = parser.newReduceAction(item.getProduction());
					if(item.lookaheadLength() > 0){
						Class<? extends Symbol<V>> lookahead = item.lookahead(0);
						if(containsKeys(actionTable, cur, lookahead)){
							System.err.println("Warning: grammar "+g+"  contains ambiguous actions.");
							System.err.println("Current: "+actionTable.get(cur).get(lookahead));
							System.err.println("Replacement: "+action);
						}
						add(actionTable, cur, lookahead, action);
					}else{
						for(Class<? extends Symbol<V>> terminal : g.getTerminalSymbolTypes()){
							if(containsKeys(actionTable, cur, terminal)){
								System.err.println("Warning: grammar "+g+"  contains ambiguous actions.");
								System.err.println("Current: "+actionTable.get(cur).get(terminal));
								System.err.println("Replacement: "+action);
							}
							add(actionTable, cur, terminal, action);
						}
						add(actionTable, cur, null, action);
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
	
	private static <V> Set<State<V>> initializeStatesSet(Parser<V> parser){
		Set<State<V>> states = new HashSet<State<V>>();
		states.add(parser.getStartState());
		return states;
	}
	
	public static <V> State<V> gotoo(Parser<V> parser, State<V> state, Class<? extends Symbol<V>> symbol){
		Set<Item<V>> shifted = new HashSet<Item<V>>();
		for(Item<V> item : state){
			if(item.hasNext() && symbol.equals(item.next())){
				shifted.add(parser.incrementItem(item));
			}
		}
		return closure(parser, shifted);
	}
	
	public static <V> State<V> closure(Parser<V> parser, Set<Item<V>> items){
		Queue<Item<V>> itemQ = new LinkedList<Item<V>>(items);
		while(!itemQ.isEmpty()){
			Item<V> item = itemQ.poll();
			if(item.hasNext()){
				Class<? extends Symbol<V>> next = item.next();
				List<Class<? extends Symbol<V>>> symbols = item.beta();
				symbols.addAll(item.lookahead());
				Set<Class<? extends Symbol<V>>> firstSet = parser.getFirst(symbols);
				for(Production<V> p : parser.getProductions(next)){
					for(Class<? extends Symbol<V>> symbol : firstSet){
						Item<V> newItem = parser.newItem(p, ListUtil.<Class<? extends Symbol<V>>>asList(symbol));
						if(items.add(newItem)){
							itemQ.add(newItem);
						}
					}
				}
			}
		}
		return parser.newState(items);
	}
	
	public static <V> Map<Class<? extends Symbol<V>>,Set<Class<? extends Symbol<V>>>> computeFollowSets(
			Grammar<V> g,
			Set<Class<? extends Symbol<V>>> nullables,
			Map<Class<? extends Symbol<V>>,Set<Class<? extends Symbol<V>>>> firstSets
			){
		
		Map<Class<? extends Symbol<V>>,Set<Class<? extends Symbol<V>>>> followSets = new HashSetHashMap<Class<? extends Symbol<V>>,Class<? extends Symbol<V>>>();
		boolean changed;
		Class<? extends Symbol<V>> lhs;
		List<Class<? extends Symbol<V>>> rhs;
		Set<Class<? extends Symbol<V>>> follow;
		do{
			changed = false;
			for(Production<V> p : g){
				lhs = p.leftHandSide();
				rhs = p.rightHandSide();
				for(int i=0; i<rhs.size()-1; ++i){
					if(!g.isTerminal(rhs.get(i))){
						follow = followSets.get(rhs.get(i));
						for(int j=i+1; j<rhs.size(); ++j){
							changed = follow.addAll(firstSets.get(rhs.get(j))) || changed;
						}
						followSets.put(rhs.get(i), follow);
					}
				}
				for(int i=rhs.size()-1; i>=0; --i){
					if(!g.isTerminal(rhs.get(i)) && nullable(rhs, i+1, rhs.size(), nullables)){
						follow = followSets.get(rhs.get(i));
						changed = follow.addAll(followSets.get(lhs)) || changed;
						followSets.put(rhs.get(i), follow);
					}
				}
			}
		}while(changed);
		return followSets;
	}
	
	public static <V> Map<Class<? extends Symbol<V>>,Set<Class<? extends Symbol<V>>>> computeFirstSets(
			Grammar<V> g,
			Set<Class<? extends Symbol<V>>> nullables
			){
		
		Map<Class<? extends Symbol<V>>,Set<Class<? extends Symbol<V>>>> firstSets = new HashSetHashMap<Class<? extends Symbol<V>>,Class<? extends Symbol<V>>>();
		for(Class<? extends Symbol<V>> terminal : g.getTerminalSymbolTypes()){
			Set<Class<? extends Symbol<V>>> set = firstSets.get(terminal);
			set.add(terminal);
			firstSets.put(terminal, set);
		}
		boolean changed;
		Class<? extends Symbol<V>> lhs;
		List<Class<? extends Symbol<V>>> rhs;
		do{
			changed = false;
			for(Production<V> p : g){
				lhs = p.leftHandSide();
				rhs = p.rightHandSide();
				if(rhs.size() > 0){
					Set<Class<? extends Symbol<V>>> first = firstSets.get(lhs);
					changed = first.addAll(firstSets.get(rhs.get(0))) || changed;
					for(int i=1; i<rhs.size(); ++i){
						if(nullable(rhs, 0, i, nullables)){
							changed = first.addAll(firstSets.get(rhs.get(i))) || changed;
						}
					}
					firstSets.put(lhs, first);
				}
			}
		}while(changed);
		return firstSets;
	}
	
	public static <V> Set<Class<? extends Symbol<V>>> computeNullables(Grammar<V> g){
		Set<Class<? extends Symbol<V>>> nullables = new HashSet<Class<? extends Symbol<V>>>();
		boolean changed;
		Class<? extends Symbol<V>> lhs;
		List<Class<? extends Symbol<V>>> rhs;
		do{
			changed = false;
			for(Production<V> p : g){
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
	
	private static <S> boolean nullable(List<S> symbols, int start, int stop, Set<S> nullables){
		for(int i=start; i<stop; ++i){
			if(!nullables.contains(symbols.get(i))){
				return false;
			}
		}
		return true;
	}
	
	private static <S> boolean nullable(List<S> symbols, Set<S> nullables){
		for(S symbol : symbols){
			if(!nullables.contains(symbol)){
				return false;
			}
		}
		return true;
	}
	
}
