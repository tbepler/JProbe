package plugins.testDataAndFunction.params;

import jprobe.services.function.DoubleArgument;

public class DoubleArg extends DoubleArgument<FieldParams>{
	
	public static final String NAME = "Double";
	public static final String TOOLTIP = "A double argument";
	public static final String CATEGORY = "Field";
	public static final double STARTVAL = 0;
	public static final double MIN = Double.NEGATIVE_INFINITY;
	public static final double MAX = Double.POSITIVE_INFINITY;
	public static final double INCREMENT = 0.1;
	
	public DoubleArg(){
		this(false);
	}
	
	public DoubleArg(boolean optional){
		this(NAME, TOOLTIP, CATEGORY, optional, STARTVAL, MIN, MAX, INCREMENT);
	}

	public DoubleArg(String name, String tooltip, String category, boolean optional, double startValue, double min, double max, double increment) {
		super(name, tooltip, category, optional, startValue, min, max, increment);
	}

	@Override
	protected void process(FieldParams params, Double value) {
		params.DOUBLE = value;
	}

}
