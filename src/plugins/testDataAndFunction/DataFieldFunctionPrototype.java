package plugins.testDataAndFunction;

import jprobe.services.data.Data;
import jprobe.services.data.DoubleField;
import jprobe.services.data.Field;
import jprobe.services.data.IntegerField;
import jprobe.services.function.BasicDataParameter;
import jprobe.services.function.BasicFieldParameter;
import jprobe.services.function.DataParameter;
import jprobe.services.function.FieldParameter;
import jprobe.services.function.Function;
import jprobe.services.function.FunctionPrototype;
import jprobe.services.function.InvalidArgumentsException;

public class DataFieldFunctionPrototype implements FunctionPrototype{
	
	private static final DataParameter[] DATA_PARAMS = new DataParameter[]{
		new BasicDataParameter("Data", "A TestData object", false, TestData.class)
	};
	
	private static final FieldParameter[] FIELD_PARAMS = new FieldParameter[]{
		new BasicFieldParameter("String", "Any string", false, new StringField("initial value")),
		new BasicFieldParameter("Integer", "An integer value", false, new IntField(0)),
		new BasicFieldParameter("Decimal", "A decimal value", false, new DecimalField(0.0))
	};
	
	@Override
	public String getFunctionName() {
		return DataFieldFunction.NAME;
	}

	@Override
	public String getFunctionDescription() {
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
	public Function newInstance(Data[] dataArgs, Field[] fieldArgs) throws InvalidArgumentsException {
		try{
			return new DataFieldFunction((TestData)dataArgs[0], fieldArgs[0].asString(), ((IntegerField)fieldArgs[1]).getValue(), ((DoubleField)fieldArgs[2]).getValue());
		} catch (Exception e){
			throw new InvalidArgumentsException(e);
		}
	}

}
