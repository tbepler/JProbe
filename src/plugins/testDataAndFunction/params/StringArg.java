package plugins.testDataAndFunction.params;

import jprobe.services.function.StringArgument;

public class StringArg extends StringArgument<FieldParams>{
	
	public static final String NAME = "String";
	public static final String TOOLTIP = "A string argument";
	public static final String CATEGORY = "Field";
	public static final String START_VAL = "string";
	
	public StringArg(){
		this(false);
	}
	
	public StringArg(boolean optional){
		this(NAME, TOOLTIP, CATEGORY, optional, START_VAL);
	}

	public StringArg(String name, String tooltip, String category, boolean optional, String startValue) {
		super(name, tooltip, category, optional, startValue);
	}

	@Override
	protected boolean isValid(String s) {
		return true;
	}

	@Override
	protected void process(FieldParams params, String s) {
		params.STRING = s;
	}

}
