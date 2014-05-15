package plugins.testDataAndFunction;

import util.progress.ProgressListener;
import jprobe.services.data.Data;
import jprobe.services.data.Field;
import jprobe.services.function.DataParameter;
import jprobe.services.function.FieldParameter;
import jprobe.services.function.Function;

public class TestFunction implements Function{
	
	public static final String NAME = "Test Function";
	public static final String DESCRIPTION = "This is a test function. It takes no arguments and produces a TestData object.";

	private static final DataParameter[] DATA_PARAMETERS = new DataParameter[]{};
	private static final FieldParameter[] FIELD_PARAMETERS	= new FieldParameter[]{};
	
	@Override
	public String getName() {
		return TestFunction.NAME;
	}

	@Override
	public String getDescription() {
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
	public Data run(ProgressListener listener, Data[] dataArgs, Field[] fieldArgs) throws Exception {
		return new TestData();
	}

}
