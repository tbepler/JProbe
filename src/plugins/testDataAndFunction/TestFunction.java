package plugins.testDataAndFunction;

import jprobe.services.data.Data;
import jprobe.services.function.Function;
import jprobe.services.function.ProgressListener;

public class TestFunction implements Function{
	
	public static final String NAME = "Test Function";
	public static final String DESCRIPTION = "This is a test function. It takes no arguments and produces a TestData object.";
	
	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public String getDescription() {
		return DESCRIPTION;
	}

	@Override
	public Data run() throws Exception {
		return new TestData();
	}

	@Override
	public boolean isProgressTrackable() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getProgressLength() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void addListener(ProgressListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeListener(ProgressListener listener) {
		// TODO Auto-generated method stub
		
	}

	
	
}
