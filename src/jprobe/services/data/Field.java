package jprobe.services.data;

import java.io.Serializable;

public interface Field extends Serializable{
	
	public String asString();
	public Field parseString(String s) throws Exception;
	public boolean isValid(String s);
	public boolean isCharacterAllowed(char c);
	public String getTooltip();
	
	
}
