package jprobe.services.data;


public abstract class DoubleField implements Field{
	private static final long serialVersionUID = 1L;
	
	public static final String DOUBLE_REGEX = "\\d+\\.\\d+";
	public static final char[] VALID_CHARS = new char[]{'.', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
	
	public abstract double getValue();
	public abstract boolean isValid(double value);
	public abstract double getMin();
	public abstract double getMax();
	public abstract double getIncrement();
	public abstract DoubleField parseDouble(double value);
	
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
	public Field parseString(String s){
		if(isValid(s)){
			return parseDouble(Double.parseDouble(s));
		}
		return null;
	}
	
	@Override
	public char[] getValidChars(){
		return VALID_CHARS;
	}
	
}
