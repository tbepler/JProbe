package util.genome;

import java.util.Collection;
import java.util.HashSet;

public enum Strand {

	PLUS,
	MINUS,
	UNKNOWN;
	
	@Override
	public String toString(){
		switch(this){
		case PLUS:
			return "+";
		case MINUS:
			return "-";
		default:
			return ".";
		}
	}
	
	public static Strand parseStrand(String s){
		if(s.equals(PLUS.toString())) return PLUS;
		if(s.equals(MINUS.toString())) return MINUS;
		return UNKNOWN;
	}
	
	public static Collection<Character> getStrandChars(){
		Collection<Character> chars = new HashSet<Character>();
		chars.add('+');
		chars.add('-');
		chars.add('.');
		return chars;
	}
	
	
}
