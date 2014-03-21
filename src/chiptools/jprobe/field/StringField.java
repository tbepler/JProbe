package chiptools.jprobe.field;

import chiptools.Constants;
import jprobe.services.data.Field;

public class StringField implements Field{
	private static final long serialVersionUID = 1L;
	
	private final String m_Value;
	private final String m_Tooltip;
	
	public StringField(String value){
		m_Value = value;
		m_Tooltip = Constants.STRING_FIELD_TOOLTIP;
	}
	
	public StringField(String value, String tooltip){
		m_Value = value;
		m_Tooltip = tooltip;
	}
	
	@Override
	public String asString() {
		return m_Value;
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
		return m_Tooltip;
	}

}
