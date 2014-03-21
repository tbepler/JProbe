package chiptools.jprobe.field;

import jprobe.services.data.DoubleField;


public class DecimalField extends DoubleField{
	private static final long serialVersionUID = 1L;

	private final double m_Val;
	private final String m_Tooltip;
	
	public DecimalField(double val, String tooltip){
		m_Val = val;
		m_Tooltip = tooltip;
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
		return Double.NEGATIVE_INFINITY;
	}

	@Override
	public double getMax() {
		return Double.POSITIVE_INFINITY;
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
