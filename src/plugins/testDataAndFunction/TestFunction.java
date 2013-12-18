package plugins.testDataAndFunction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jprobe.services.Data;
import jprobe.services.DataField;
import jprobe.services.Function;

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
	public Data run(List<Data> requiredData, List<Data> optionalData,
			List<DataField> requiredFields, List<DataField> optionalFields)
			throws Exception {
		return new TestData();
	}

	
	
}
