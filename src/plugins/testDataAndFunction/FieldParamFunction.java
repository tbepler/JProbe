package plugins.testDataAndFunction;

import java.util.ArrayList;
import java.util.Collection;

import plugins.testDataAndFunction.params.*;
import util.progress.ProgressListener;
import jprobe.services.data.Data;
import jprobe.services.function.Argument;

public class FieldParamFunction extends AbstractTestFunction<FieldParams>{
	
	public static final String NAME = "fields";
	public static final String DESCRIPTION = "A function that takes field parameters and returns a test data object with those values";
	
	public FieldParamFunction(){
		super(NAME, DESCRIPTION, FieldParams.class);
	}

	@Override
	public Collection<Argument<? super FieldParams>> getArguments() {
		Collection<Argument<? super FieldParams>> args = new ArrayList<Argument<? super FieldParams>>();
		args.add(new StringArg(false));
		args.add(new IntArg(false));
		args.add(new DoubleArg(false));
		return args;
	}

	@Override
	public Data execute(ProgressListener l, FieldParams params) throws Exception {
		return new TestData(params.STRING, params.INT, params.DOUBLE);
	}

}
