package jprobe.services.data;

import java.io.Serializable;

public interface DataField extends Serializable{
	
	public String asString();
	public DataField parseString(String s);
	public boolean isValid(String s);
	public char[] getValidChars();
	public String getTooltip();
	
	
}
