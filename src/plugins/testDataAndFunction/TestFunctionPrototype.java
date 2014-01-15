package plugins.testDataAndFunction;

import jprobe.services.function.DataParameter;
import jprobe.services.function.FieldParameter;
import jprobe.services.function.Function;
import jprobe.services.function.FunctionPrototype;
import jprobe.services.function.InvalidArgumentsException;

public class TestFunctionPrototype implements FunctionPrototype{

	private static final DataParameter[] DATA_PARAMETERS = new DataParameter[]{};
	private static final FieldParameter[] FIELD_PARAMETERS	= new FieldParameter[]{};
	
	@Override
	public String getFunctionName() {
		return TestFunction.NAME;
	}

	@Override
	public String getFunctionDescription() {
		return TestFunction.DESCRIPTION;
	}

	@Override
	public DataParameter[] getDataParameters() {
		return DATA_PARAMETERS;
	}

	@Override
	public FieldParameter[] getFieldParameters() {
		return FIELD_PARAMETERS;
	}

	@Override
	public Function newInstance(DataParameter[] dataParams,
			FieldParameter[] fieldParams) throws InvalidArgumentsException {
		return new TestFunction();
	}

}
