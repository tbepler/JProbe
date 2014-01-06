package plugins.testDataAndFunction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jprobe.services.Data;
import jprobe.services.DataField;
import jprobe.services.Function;
import jprobe.services.FunctionListener;
import jprobe.services.FunctionParam;

public class TestFunction implements Function{
	
	public static final String NAME = "Test Function";
	public static final String DESCRIPTION = "This is a test function. It takes no arguments and produces a TestData object.";
	private List<Class<? extends Data>> data = new ArrayList<Class<? extends Data>>();
	private List<DataField> fields = new ArrayList<DataField>();
	
	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public String getDescription() {
		return DESCRIPTION;
	}

	@Override
	public List<Class<? extends Data>> getRequiredDataArgs() {
		return Collections.unmodifiableList(data);
	}

	@Override
	public List<Class<? extends Data>> getOptionalDataArgs() {
		return Collections.unmodifiableList(data);
	}

	@Override
	public List<DataField> getRequiredFields() {
		return Collections.unmodifiableList(fields);
	}

	@Override
	public List<DataField> getOptionalFields() {
		return Collections.unmodifiableList(fields);
	}

	@Override
	public Data run(FunctionParam params) throws Exception {
		return new TestData();
	}

	@Override
	public boolean isProgressTrackable() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getProgressLength(FunctionParam params) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void addListener(FunctionListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeListener(FunctionListener listener) {
		// TODO Auto-generated method stub
		
	}

	
	
}
