package plugins.testDataAndFunction.params;

import jprobe.services.function.IntArgument;

public class IntArg extends IntArgument<FieldParams>{
	
	public static final String NAME = "int";
	public static final String TOOLTIP = "An integer argument";
	public static final String CATEGORY = "Field";
	public static final Character FLAG = 'i';
	public static final String PROTOTYPE = "INT";
	public static final int STARTVAL = 0;
	public static final int MIN = Integer.MIN_VALUE;
	public static final int MAX = Integer.MAX_VALUE;
	public static final int INCREMENT = 1;
	
	public IntArg(){
		this(false);
	}
	
	public IntArg(boolean optional){
		this(NAME, TOOLTIP, CATEGORY, FLAG, PROTOTYPE, optional, STARTVAL, MIN, MAX, INCREMENT);
	}

	public IntArg(String name, String tooltip, String category, Character flag, String prot, boolean optional, int startValue, int min, int max, int increment) {
		super(name, tooltip, category, flag, prot, optional, startValue, min, max, increment);
	}

	@Override
	protected void process(FieldParams params, Integer value) {
		params.INT = value;
	}

}
