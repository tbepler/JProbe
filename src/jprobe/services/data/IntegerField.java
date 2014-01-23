package jprobe.services.data;

import java.util.HashSet;
import java.util.Set;


public abstract class IntegerField implements Field{
	private static final long serialVersionUID = 1L;
	
	public static final String INT_REGEX = "^-?\\d+$";
	public static final char[] VALID_CHARS = new char[]{'-', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
	private static final Set<Character> VALID_CHARS_SET = generateValidChars();
	
	public abstract int getValue();
	public abstract boolean isValid(int value);
	public abstract int getMin();
	public abstract int getMax();
	public abstract int getIncrement();
	public abstract IntegerField parseInt(int value) throws Exception;
	
	private static Set<Character> generateValidChars(){
		Set<Character> valid = new HashSet<Character>();
		for(char c : VALID_CHARS){
			valid.add(c);
		}
		return valid;
	}
	
	@Override
	public String asString(){
		return String.valueOf(this.getValue());
	}
	
	@Override
	public Field parseString(String s) throws Exception{
		if(isValid(s)){
			return parseInt(Integer.parseInt(s));
		}
		return null;
	}
	
	@Override
	public boolean isValid(String s){
		if(s.matches(INT_REGEX)){
			return isValid(Integer.parseInt(s));
		}
		return false;
	}
	
	@Override
	public boolean isCharacterAllowed(char c){
		return VALID_CHARS_SET.contains(c);
	}
	
}
