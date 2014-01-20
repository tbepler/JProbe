package plugins.testDataAndFunction;

import jprobe.services.data.Field;

public class StringField implements Field{
	private static final long serialVersionUID = 1L;

	public static final String TOOLTIP = "A field containing any String";
	
	private String value;
	
	public StringField(String value){
		this.value = value;
	}
	
	@Override
	public String asString() {
		return value;
	}

	@Override
	public Field parseString(String s) {
		return new StringField(s);
	}

	@Override
	public boolean isValid(String s) {
		return true;
	}

	@Override
	public boolean isCharacterAllowed(char c){
		return true;
	}

	@Override
	public String getTooltip() {
		return TOOLTIP;
	}

}
