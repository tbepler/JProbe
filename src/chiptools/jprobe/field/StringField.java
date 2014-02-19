package chiptools.jprobe.field;

import chiptools.Constants;
import jprobe.services.data.Field;

public class StringField implements Field{
	private static final long serialVersionUID = 1L;
	
	private final String value;
	
	public StringField(String value){
		this.value = value;
	}
	
	@Override
	public String asString() {
		return value;
	}

	@Override
	public Field parseString(String s) throws Exception{
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
		return Constants.STRING_FIELD_TOOLTIP;
	}

}
