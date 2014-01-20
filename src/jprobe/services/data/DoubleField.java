package jprobe.services.data;

import java.util.HashSet;
import java.util.Set;


public abstract class DoubleField implements Field{
	private static final long serialVersionUID = 1L;
	
	public static final String DOUBLE_REGEX = "\\d+\\.\\d+";
	public static final char[] VALID_CHARS = new char[]{'.', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
	private static final Set<Character> VALID_CHARS_SET = generateValidChars();
	
	public abstract double getValue();
	public abstract boolean isValid(double value);
	public abstract double getMin();
	public abstract double getMax();
	public abstract double getIncrement();
	public abstract DoubleField parseDouble(double value) throws Exception;
	
	private static Set<Character> generateValidChars(){
		Set<Character> valid = new HashSet<Character>();
		for(char c : VALID_CHARS){
			VALID_CHARS_SET.add(c);
		}
		return valid;
	}
	
	@Override
	public String asString(){
		return String.valueOf(this.getValue());
	}
	
	@Override
	public boolean isValid(String s){
		if(s.matches(DOUBLE_REGEX)){
			return isValid(Double.parseDouble(s));
		}
		return false;
	}
	
	@Override
	public Field parseString(String s) throws Exception{
		if(isValid(s)){
			return parseDouble(Double.parseDouble(s));
		}
		return null;
	}
	
	@Override
	public boolean isCharacterAllowed(char c){
		return VALID_CHARS_SET.contains(c);
	}
	
}
