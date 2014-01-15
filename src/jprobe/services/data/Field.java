package jprobe.services.data;

import java.io.Serializable;

public interface Field extends Serializable{
	
	public String asString();
	public Field parseString(String s);
	public boolean isValid(String s);
	public char[] getValidChars();
	public String getTooltip();
	
	
}
