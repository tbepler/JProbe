package jprobe.framework.model.compiler;

import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jprobe.framework.model.compiler.lexer.Token;

public class Parser<S> {
	
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
