package jprobe.framework.model.compiler;

import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jprobe.framework.model.compiler.lexer.Token;

public class Parser<S> {
	
	private static class Item<S>{
		
		private final S lhs;
		private final S[] rhs;
		private final int index;
		private final int hash;
		
		public Item(Production<S> prod){
			this(prod.leftHandSide(), prod.rightHandSide());
		}
		
		public Item(S lhs, S ... rhs){
			this(0, lhs, rhs);
		}
		
		private Item(int index, S lhs, S ... rhs){
			this.index = index;
			this.lhs = lhs;
			this.rhs = rhs;
			hash = Arrays.hashCode(new Object[]{index, lhs, rhs});
		}
		
		public Item<S> increment(){
			return new Item<S>(index+1, lhs, rhs);
		}
		
		public int index(){
			return index;
		}
		
		public boolean hasNext(){
			return index < rhs.length;
		}
		
		public S nextSymbol(){
			return rhs[index];
		}
		
		public S leftHandSide(){
			return lhs;
		}
		
		@Override
		public boolean equals(Object o){
			if(o == null) return false;
			if(o == this) return true;
			if(o instanceof Item){
				Item<?> i = (Item<?>) o;
				return index == i.index && lhs.equals(i.lhs) && Arrays.equals(rhs, i.rhs);
			}
			return false;
		}
		
		@Override
		public int hashCode(){
			return hash;
		}
		
	}
	
	private static class State<S>{
		
		private final Set<Item<S>> items;
		private final int hash;
		
		public State(Set<Item<S>> items){
			this.items = new HashSet<Item<S>>(items);
			hash = Arrays.hashCode(items.toArray());
		}
		
		public Set<Item<S>> asSet(){
			return Collections.unmodifiableSet(items);
		}
		
		@Override
		public int hashCode(){
			return hash;
		}
		
		@Override
		public boolean equals(Object o){
			if(o == null) return false;
			if(o == this) return true;
			if(o instanceof State){
				State<?> s = (State<?>) o;
				if(items.size() == s.items.size()){
					for(Item<S> item : items){
						if(!s.items.contains(item)){
							return false;
						}
					}
					return true;
				}
			}
			return false;
		}
		
	}
	
	private static class Action{
		
	}
	
	private final Grammar<S> m_Grammar;
	private final Set<S> m_Nullable;
	private final Map<S,Set<S>> m_First;
	private final Map<S,Set<S>> m_Follow;
	
	public Parser(Grammar<S> g){
		m_Grammar = g;
		m_Nullable = this.computeNullables();
		m_First = this.computeFirstSets();
		m_Follow = this.computeFollowSets();
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
	
	public Set<S> getFirst(S symbol){
		return Collections.unmodifiableSet(m_First.get(symbol));
	}
	
	public Set<S> getFollow(S symbol){
		return Collections.unmodifiableSet(m_Follow.get(symbol));
	}
	
	public boolean isNullable(S symbol){
		return m_Nullable.contains(symbol);
	}
	
	public void parse(List<Token> tokens){
		
	}
	
	private Map<State<S>,Map<S,Action>> constructParseTable(){
		
	}
	
	private Set<Item<S>> gotoo(Set<Item<S>> items, S symbol){
		Set<Item<S>> shifted = new HashSet<Item<S>>();
		for(Item<S> item : items){
			if(item.hasNext() && symbol.equals(item.nextSymbol())){
				shifted.add(item.increment());
			}
		}
		return this.closure(shifted);
	}
	
	private Set<Item<S>> closure(Set<Item<S>> items){
		S next;
		boolean changed;
		do{
			changed = false;
			Set<Item<S>> copy = new HashSet<Item<S>>(items);
			for(Item<S> item : items){
				if(item.hasNext()){
					next = item.nextSymbol();
					for(Production<S> p : m_Grammar.getProductions(next)){
						Item<S> newItem = new Item<S>(p);
						changed = copy.add(newItem) || changed;
					}
				}
			}
			items = copy;
		}while(changed);
		return items;
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
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
