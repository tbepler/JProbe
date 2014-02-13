package plugins.genome.services.utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public enum DNA{
	
	A('A'),
	C('C'),
	G('G'),
	T('T');
	
	private static final Map<DNA, DNA> COMPLIMENTS = generateCompliments();
	private static final Map<DNA, Set<DNA>> MATCHES = generateMatches();
	
	private static Map<DNA, DNA> generateCompliments(){
		Map<DNA, DNA> map = new HashMap<DNA, DNA>();
		map.put(A, T);
		map.put(T, A);
		map.put(G, C);
		map.put(C, G);
		return map;
	}
	
	private static Map<DNA, Set<DNA>> generateMatches(){
		Map<DNA, Set<DNA>> map = new HashMap<DNA, Set<DNA>>();
		Set<DNA> matches = new HashSet<DNA>();
		//add A
		matches.add(A);
		map.put(A, matches);
		//add T
		matches = new HashSet<DNA>();
		matches.add(T);
		map.put(T, matches);
		//add G
		matches = new HashSet<DNA>();
		matches.add(G);
		map.put(G, matches);
		//add C
		matches = new HashSet<DNA>();
		matches.add(C);
		map.put(C, matches);
		
		return map;
		
	}
	
	private final char m_Symbol;
	
	private DNA(char symbol){
		m_Symbol = symbol;
	}
	
	public DNA getCompliment(){
		return COMPLIMENTS.get(this);
	}
	
	public boolean matches(DNA other){
		return other == this || MATCHES.get(this).contains(other);
	}
	
	@Override
	public String toString(){
		return String.valueOf(m_Symbol);
	}
	
}
