package chiptools.jprobe.field;

import jprobe.services.data.DoubleField;


public class DecimalField extends DoubleField{
	private static final long serialVersionUID = 1L;

	private final double m_Val;
	private final String m_Tooltip;
	private final double m_Min;
	private final double m_Max;
	
	public DecimalField(double val, double min, double max, String tooltip){
		m_Val = val;
		m_Min = min;
		m_Max = max;
		m_Tooltip = tooltip;
	}
	
	public DecimalField(double val, String tooltip){
		this(val, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, tooltip);
	}
	
	@Override
	public String getTooltip() {
		return m_Tooltip;
	}

	@Override
	public double getValue() {
		return m_Val;
	}

	@Override
	public boolean isValid(double value) {
		return true;
	}

	@Override
	public double getMin() {
		return m_Min;
	}

	@Override
	public double getMax() {
		return m_Max;
	}

	@Override
	public double getIncrement() {
		return 0.1;
	}

	@Override
	public DoubleField parseDouble(double value) throws Exception {
		return new DecimalField(value, m_Tooltip);
	}

}
