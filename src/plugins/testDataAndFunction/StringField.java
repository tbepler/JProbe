package plugins.testDataAndFunction;

import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

import jprobe.services.DataField;

public class StringField implements DataField{
	
	public static final String TOOLTIP = "A field containing any String";
	public static final char[] VALID_CHARS = allLetters("UTF-8");
	
	private String value;
	
	public StringField(String value){
		this.value = value;
	}
	
	private static char[] allLetters(String encoding){
		CharsetEncoder ce = Charset.forName(encoding).newEncoder();
		StringBuilder sb = new StringBuilder();
		for(char c=0; c<Character.MAX_VALUE; c++){
			if(ce.canEncode(c)){
				sb.append(c);
			}
		}
		return sb.toString().toCharArray();
	}
	
	@Override
	public String asString() {
		return value;
	}

	@Override
	public DataField parseString(String s) {
		return new StringField(s);
	}

	@Override
	public boolean isValid(String s) {
		return true;
	}

	@Override
	public char[] getValidChars() {
		return VALID_CHARS;
	}

	@Override
	public String getTooltip() {
		return TOOLTIP;
	}

}
