package util;

import java.util.*;

public class DNAUtils {
	
	private static final Map<Character, Character> COMPLIMENTS = genCompliments();
			
	private static Map<Character, Character> genCompliments(){
		Map<Character, Character> map = new HashMap<Character, Character>();
		map.put('a', 't');
		map.put('A', 'T');
		map.put('c', 'g');
		map.put('C', 'G');
		map.put('g', 'c');
		map.put('G', 'C');
		map.put('t', 'a');
		map.put('T', 'A');
		return map;
	}
	
	public static String reverseCompliment(String seq){
		return reverse(compliment(seq));
	}
	
	public static String reverse(String seq){
		String rev = "";
		for(char c : seq.toCharArray()){
			rev = c + rev;
		}
		return rev;
	}
	
	public static String compliment(String seq){
		String comp = "";
		for(char base : seq.toCharArray()){
			comp += compliment(base);
		}
		return comp;
	}
	
	public static char compliment(char base){
		if(COMPLIMENTS.containsKey(base)){
			return COMPLIMENTS.get(base);
		}
		return base;
	}
	
}
