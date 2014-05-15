package plugins.testDataAndFunction;

import util.progress.ProgressListener;
import jprobe.services.data.Data;
import jprobe.services.data.DoubleField;
import jprobe.services.data.Field;
import jprobe.services.function.BasicFieldParameter;
import jprobe.services.function.DataParameter;
import jprobe.services.function.FieldParameter;
import jprobe.services.function.Function;

public class FieldParamFunction implements Function{
	
	public static final String NAME = "Field Parameter Function";
	public static final String DESCRIPTION = "A function that takes field parameters and returns a test data object with those values";

	private static final FieldParameter[] PARAMS = new FieldParameter[]{
		new BasicFieldParameter("String", "The TestData string field", false, new StringField("default")),
		new BasicFieldParameter("Integer", "The TestData integer field", false, new IntField(0)),
		new BasicFieldParameter("Decimal", "The TestData decimal field", false, new DecimalField(0))	
	};
	
	@Override
	public String getName() {
		return FieldParamFunction.NAME;
	}

	@Override
	public String getDescription() {
		return FieldParamFunction.DESCRIPTION;
	}

	@Override
	public DataParameter[] getDataParameters() {
		return new DataParameter[]{};
	}

	@Override
	public FieldParameter[] getFieldParameters() {
		return PARAMS;
	}

	@Override
	public Data run(ProgressListener listener, Data[] dataArgs, Field[] fieldArgs) throws Exception {
		String s = fieldArgs[0].asString();
		int i = ((IntField)fieldArgs[1]).getValue();
		double d = ((DoubleField)fieldArgs[2]).getValue();
		return new TestData(s, i, d);
	}

}
