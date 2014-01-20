package plugins.testDataAndFunction;

import jprobe.services.data.Data;
import jprobe.services.data.Field;
import jprobe.services.function.BasicDataParameter;
import jprobe.services.function.DataParameter;
import jprobe.services.function.FieldParameter;
import jprobe.services.function.Function;
import jprobe.services.function.FunctionPrototype;
import jprobe.services.function.InvalidArgumentsException;

public class DataParamFunctionPrototype implements FunctionPrototype{
	
	private static final DataParameter[] DATA_PARAMS = new DataParameter[]{
		new BasicDataParameter("TestData1", TestData.TOOLTIP, false, TestData.class),
		new BasicDataParameter("TestData2", TestData.TOOLTIP, false, TestData.class),
		new BasicDataParameter("TestData3", TestData.TOOLTIP, false, TestData.class),
		new BasicDataParameter("TestData4", TestData.TOOLTIP, false, TestData.class)
	};
	
	@Override
	public String getFunctionName() {
		return DataParamFunction.NAME;
	}

	@Override
	public String getFunctionDescription() {
		return DataParamFunction.DESCRIPTION;
	}

	@Override
	public DataParameter[] getDataParameters() {
		return DATA_PARAMS;
	}

	@Override
	public FieldParameter[] getFieldParameters() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Function newInstance(Data[] dataArgs, Field[] fieldArgs) throws InvalidArgumentsException {
		for(Data d : dataArgs){
			if(!(d instanceof TestData)){
				throw new InvalidArgumentsException(d.getClass() + " not a legal argument");
			}
		}
		return new DataParamFunction(dataArgs);
	}

}
