package plugins.testDataAndFunction;

import jprobe.services.data.DoubleField;

public class DecimalField extends DoubleField{
	private static final long serialVersionUID = 1L;

	public static final String TOOLTIP = "A decimal field with no bounds";
	
	private double value;
	
	public DecimalField(double value){
		this.value = value;
	}
	
	@Override
	public String getTooltip() {
		return TOOLTIP;
	}

	@Override
	public double getValue() {
		return value;
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
	public DoubleField parseDouble(double value) throws Exception{
		return new DecimalField(value);
	}

}
