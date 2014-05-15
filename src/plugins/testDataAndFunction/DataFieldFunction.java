package plugins.testDataAndFunction;

import util.progress.ProgressListener;
import jprobe.services.data.Data;
import jprobe.services.data.DoubleField;
import jprobe.services.data.Field;
import jprobe.services.data.IntegerField;
import jprobe.services.function.BasicDataParameter;
import jprobe.services.function.BasicFieldParameter;
import jprobe.services.function.DataParameter;
import jprobe.services.function.FieldParameter;
import jprobe.services.function.Function;

public class DataFieldFunction implements Function{
	
	public static final String NAME = "Data and Fields Function";
	public static final String DESCRIPTION = "A function that requires both data and field parameters";
	
	private static final DataParameter[] DATA_PARAMS = new DataParameter[]{
		new BasicDataParameter("Data", "A TestData object", false, TestData.class)
	};
	
	private static final FieldParameter[] FIELD_PARAMS = new FieldParameter[]{
		new BasicFieldParameter("String", "Any string", false, new StringField("initial value")),
		new BasicFieldParameter("Integer", "An integer value", false, new IntField(0)),
		new BasicFieldParameter("Decimal", "A decimal value", false, new DecimalField(0.0))
	};
	
	@Override
	public String getName() {
		return DataFieldFunction.NAME;
	}

	@Override
	public String getDescription() {
		return DataFieldFunction.DESCRIPTION;
	}

	@Override
	public DataParameter[] getDataParameters() {
		return DATA_PARAMS;
	}

	@Override
	public FieldParameter[] getFieldParameters() {
		return FIELD_PARAMS;
	}

	@Override
	public Data run(ProgressListener listener, Data[] dataArgs, Field[] fieldArgs) throws Exception {
		TestData data = (TestData) dataArgs[0];
		String s = fieldArgs[0].asString();
		int i = ((IntegerField)fieldArgs[1]).getValue();
		double d = ((DoubleField)fieldArgs[2]).getValue();
		return new TestData(data.getString()+s,data.getInt()+i, data.getDouble()+d);
	}

}
