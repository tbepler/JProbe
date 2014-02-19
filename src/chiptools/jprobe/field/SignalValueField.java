package chiptools.jprobe.field;

import chiptools.Constants;
import jprobe.services.data.DoubleField;

public class SignalValueField extends DoubleField{
	private static final long serialVersionUID = 1L;
	
	private final double m_Val;
	
	public SignalValueField(double value){
		if(!this.isValid(value)){
			throw new RuntimeException("Error: value="+value+" must be greater than or equal to "+Constants.SIGNAL_VALUE_MIN);
		}
		m_Val = value;
	}
	
	@Override
	public String getTooltip() {
		return Constants.SIGNAL_VALUE_FIELD_TOOLTIP;
	}

	@Override
	public double getValue() {
		return m_Val;
	}

	@Override
	public boolean isValid(double value) {
		return value >= Constants.SIGNAL_VALUE_MIN;
	}

	@Override
	public double getMin() {
		return Constants.SIGNAL_VALUE_MIN;
	}

	@Override
	public double getMax() {
		return Constants.SIGNAL_VALUE_MAX;
	}

	@Override
	public double getIncrement() {
		return 1.0;
	}

	@Override
	public DoubleField parseDouble(double value) throws Exception {
		try{
			return new SignalValueField(value);
		} catch (Exception e){
			throw e;
		}
	}

}
