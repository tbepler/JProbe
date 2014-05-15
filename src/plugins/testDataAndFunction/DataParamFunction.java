package plugins.testDataAndFunction;

import util.progress.ProgressListener;
import jprobe.services.data.Data;
import jprobe.services.data.Field;
import jprobe.services.function.BasicDataParameter;
import jprobe.services.function.DataParameter;
import jprobe.services.function.FieldParameter;
import jprobe.services.function.Function;

public class DataParamFunction implements Function{
	
	public static final String NAME = "Data Parameter Function";
	public static final String DESCRIPTION = "A function that requires data parameters to run. It adds all the fields of the given parameters together.";
	
	private static final DataParameter[] DATA_PARAMS = new DataParameter[]{
		new BasicDataParameter("TestData1", TestData.TOOLTIP, false, TestData.class),
		new BasicDataParameter("TestData2", TestData.TOOLTIP, false, TestData.class),
		new BasicDataParameter("TestData3", TestData.TOOLTIP, false, TestData.class),
		new BasicDataParameter("TestData4", TestData.TOOLTIP, false, TestData.class)
	};
	
	@Override
	public String getName() {
		return DataParamFunction.NAME;
	}

	@Override
	public String getDescription() {
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
	public Data run(ProgressListener listener, Data[] dataArgs, Field[] fieldArgs) throws Exception {
		String s = "";
		int i = 0;
		double d = 0;
		for(Data data : dataArgs){
			if(data instanceof TestData){
				TestData tData = (TestData) data;
				s += tData.getString();
				i += tData.getInt();
				d += tData.getDouble();
			}
		}
		return new TestData(s, i, d);
	}

}
