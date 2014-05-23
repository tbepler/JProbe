package plugins.testDataAndFunction;

import java.util.ArrayList;
import java.util.Collection;

import plugins.testDataAndFunction.params.DataArg;
import plugins.testDataAndFunction.params.DoubleArg;
import plugins.testDataAndFunction.params.FieldDataParams;
import plugins.testDataAndFunction.params.IntArg;
import plugins.testDataAndFunction.params.StringArg;
import util.progress.ProgressListener;
import jprobe.services.data.Data;
import jprobe.services.function.Argument;

public class DataFieldFunction extends AbstractTestFunction<FieldDataParams>{
	
	public static final String NAME = "dataAndFields";
	public static final String DESCRIPTION = "A function that requires both data and field parameters";
	
	public DataFieldFunction(){
		super(NAME, DESCRIPTION, FieldDataParams.class);
	}

	@Override
	public Collection<Argument<? super FieldDataParams>> getArguments() {
		Collection<Argument<? super FieldDataParams>> args = new ArrayList<Argument<? super FieldDataParams>>();
		args.add(new DataArg(Activator.getCore(), true));
		args.add(new StringArg(true));
		args.add(new IntArg(true));
		args.add(new DoubleArg(true));
		return args;
	}

	@Override
	public Data execute(ProgressListener l, FieldDataParams params) throws Exception {
		String s = ""; 
		int i = 0;
		double d = 0;
		
		for(TestData data : params.getData()){
			s += data.getString();
			i += data.getInt();
			d += data.getDouble();
		}

		if(params.STRING != null){
			s += params.STRING;
		}
		if(params.INT != null){
			i += params.INT;
		}
		if(params.DOUBLE != null){
			d += params.DOUBLE;
		}
		
		return new TestData(s, i, d);
	}

}
