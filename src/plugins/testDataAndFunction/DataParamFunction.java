package plugins.testDataAndFunction;

import java.util.ArrayList;
import java.util.Collection;

import plugins.testDataAndFunction.params.DataArg;
import plugins.testDataAndFunction.params.DataParam;
import plugins.testDataAndFunction.params.FieldDataParams;
import util.progress.ProgressListener;
import jprobe.services.data.Data;
import jprobe.services.function.Argument;

public class DataParamFunction extends AbstractTestFunction<DataParam>{
	

	public static final String NAME = "data";
	public static final String DESCRIPTION = "A function that requires data parameters to run. It adds all the fields of the given parameters together.";
	
	public DataParamFunction() {
		super(NAME, DESCRIPTION, FieldDataParams.class);
	}

	@Override
	public Collection<Argument<? super DataParam>> getArguments() {
		Collection<Argument<? super DataParam>> args = new ArrayList<Argument<? super DataParam>>();
		args.add(new DataArg(Activator.getCore(), false, 3, 5, false));
		return args;
	}

	@Override
	public Data execute(ProgressListener l, DataParam params) throws Exception {
		String s = "";
		int i = 0;
		double d = 0;
		for(TestData data : params.getData()){
			s += data.getString();
			i += data.getInt();
			d += data.getDouble();
		}
		return new TestData(s, i, d);
	}

}
