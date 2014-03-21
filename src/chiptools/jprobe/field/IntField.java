package chiptools.jprobe.field;

import jprobe.services.data.IntegerField;

public class IntField extends IntegerField{
	private static final long serialVersionUID = 1L;
	
	private final int m_Value;
	private final int m_Min;
	private final int m_Max;
	private final String m_Tooltip;
	
	public IntField(int value, int min, int max, String tooltip){
		m_Value = value;
		m_Min = min;
		m_Max = max;
		m_Tooltip = tooltip;
	}

	@Override
	public String getTooltip() {
		return m_Tooltip;
	}

	@Override
	public int getValue() {
		return m_Value;
	}

	@Override
	public boolean isValid(int value) {
		return value >= m_Min && value <= m_Max;
	}

	@Override
	public int getMin() {
		return m_Min;
	}

	@Override
	public int getMax() {
		return m_Max;
	}

	@Override
	public int getIncrement() {
		return 1;
	}

	@Override
	public IntegerField parseInt(int value) throws Exception {
		if(isValid(value)){
			return new IntField(value, m_Min, m_Max, m_Tooltip);
		}
		throw new Exception(value+" not valid value.");
	}

}
