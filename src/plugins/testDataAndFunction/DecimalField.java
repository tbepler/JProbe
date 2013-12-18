package plugins.testDataAndFunction;

import jprobe.services.DoubleField;

public class DecimalField extends DoubleField{
	
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
	public DoubleField parseDouble(double value) {
		return new DecimalField(value);
	}

}
