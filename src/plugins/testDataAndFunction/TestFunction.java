package plugins.testDataAndFunction;

import java.util.ArrayList;
import java.util.Collection;

import plugins.testDataAndFunction.params.NullParameter;
import util.progress.ProgressListener;
import jprobe.services.data.Data;
import jprobe.services.function.Argument;

public class TestFunction extends AbstractTestFunction<NullParameter>{

	public static final String NAME = "Test Function";
	public static final String DESCRIPTION = "This is a test function. It takes no arguments and produces a TestData object.";
	
	public TestFunction() {
		super(NAME, DESCRIPTION, NullParameter.class);
	}
	
	@Override
	public Collection<Argument<NullParameter>> getArguments() {
		return new ArrayList<Argument<NullParameter>>();
	}
	@Override
	public Data execute(ProgressListener l, NullParameter params) {
		return new TestData();
	}


}
