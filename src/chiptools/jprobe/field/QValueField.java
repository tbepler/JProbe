package chiptools.jprobe.field;

import chiptools.Constants;
import jprobe.services.data.DoubleField;

public class QValueField extends DoubleField{
	private static final long serialVersionUID = 1L;
	
	private final double m_Val;
	
	public QValueField(double value){
		if(!this.isValid(value)){
			throw new RuntimeException("Error: "+value+" is not a valid q-value");
		}
		m_Val = value;
	}

	@Override
	public String getTooltip() {
		return Constants.QVALUE_FIELD_TOOLTIP;
	}

	@Override
	public double getValue() {
		return m_Val;
	}

	@Override
	public boolean isValid(double value) {
		return value == -1.0 || value >= 0.0;
	}

	@Override
	public double getMin() {
		return Constants.QVALUE_MIN;
	}

	@Override
	public double getMax() {
		return Constants.QVALUE_MAX;
	}

	@Override
	public double getIncrement() {
		return 1;
	}

	@Override
	public DoubleField parseDouble(double value) throws Exception {
		try{
			return new QValueField(value);
		} catch (Exception e){
			throw e;
		}
	}

}
