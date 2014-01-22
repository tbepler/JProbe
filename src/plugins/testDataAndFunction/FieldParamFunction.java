package plugins.testDataAndFunction;

import jprobe.services.data.Data;
import jprobe.services.function.Function;
import jprobe.services.function.ProgressListener;

public class FieldParamFunction implements Function{

	public static final String NAME = "Field Parameter Function";
	public static final String DESCRIPTION = "A function that takes field parameters and returns a test data object with those values";
	
	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public String getDescription() {
		return DESCRIPTION;
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

	@Override
	public Data run() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
}
