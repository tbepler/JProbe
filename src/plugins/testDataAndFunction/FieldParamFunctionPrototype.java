package plugins.testDataAndFunction;

import jprobe.services.data.Data;
import jprobe.services.data.Field;
import jprobe.services.function.BasicFieldParameter;
import jprobe.services.function.DataParameter;
import jprobe.services.function.FieldParameter;
import jprobe.services.function.Function;
import jprobe.services.function.FunctionPrototype;
import jprobe.services.function.InvalidArgumentsException;

public class FieldParamFunctionPrototype implements FunctionPrototype{

	private static final FieldParameter[] PARAMS = new FieldParameter[]{
		new BasicFieldParameter("String", "The TestData string field", false, new StringField("")),
		new BasicFieldParameter("Integer", "The TestData integer field", false, new IntField(0)),
		new BasicFieldParameter("Decimal", "The TestData decimal field", false, new DecimalField(0))	
	};
	
	@Override
	public String getFunctionName() {
		return FieldParamFunction.NAME;
	}

	@Override
	public String getFunctionDescription() {
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
	public Function newInstance(Data[] dataArgs, Field[] fieldArgs) throws InvalidArgumentsException {
		try{
			return new FieldParamFunction(fieldArgs);
		} catch (Exception e){
			throw new InvalidArgumentsException(e);
		}
	}

}
