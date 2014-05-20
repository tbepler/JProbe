package plugins.testDataAndFunction;

import java.util.Collection;

import plugins.testDataAndFunction.params.DataParam;
import util.progress.ProgressListener;
import jprobe.services.data.Data;
import jprobe.services.data.Field;
import jprobe.services.function.Argument;
import jprobe.services.function.BasicDataParameter;
import jprobe.services.function.DataParameter;
import jprobe.services.function.FieldParameter;
import jprobe.services.function.Function;

public class DataParamFunction extends AbstractTestFunction<DataParam>{
	

	public static final String NAME = "Data Parameter Function";
	public static final String DESCRIPTION = "A function that requires data parameters to run. It adds all the fields of the given parameters together.";
	
	public DataParamFunction() {
		super(NAME, DESCRIPTION, DataParam.class);
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

	@Override
	public Collection<Argument<? super DataParam>> getArguments() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Data execute(ProgressListener l, DataParam params) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
