package jprobe.services.data;


public abstract class IntegerField implements Field{
	private static final long serialVersionUID = 1L;
	
	public static final String INT_REGEX = "\\d+";
	public static final char[] VALID_CHARS = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
	
	public abstract int getValue();
	public abstract boolean isValid(int value);
	public abstract int getMin();
	public abstract int getMax();
	public abstract int getIncrement();
	public abstract IntegerField parseInt(int value);
	
	@Override
	public String asString(){
		return String.valueOf(this.getValue());
	}
	
	@Override
	public Field parseString(String s){
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
	public char[] getValidChars(){
		return VALID_CHARS;
	}
	
}
