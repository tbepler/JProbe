package plugins.testDataAndFunction.params;

import java.util.List;

import plugins.testDataAndFunction.TestData;
import jprobe.services.JProbeCore;
import jprobe.services.function.DataArgument;

public class DataArg extends DataArgument<DataParam,TestData>{
	
	public static final String NAME = "Data";
	public static final String TOOLTIP = "Data arguments";
	public static final String CATEGORY = "Data";

	public DataArg(JProbeCore core, boolean allowDuplicates){
		this(core, true, 0, Integer.MAX_VALUE, allowDuplicates);
	}
	
	public DataArg(JProbeCore core, boolean optional, int minArgs, int maxArgs, boolean allowDuplicates){
		this(core, NAME, TOOLTIP, CATEGORY, optional, TestData.class, minArgs, maxArgs, allowDuplicates);
	}
	
	public DataArg(JProbeCore core, String name, String tooltip,
			String category, boolean optional, Class<TestData> dataClass,
			int minArgs, int maxArgs, boolean allowDuplicates) {
		super(core, name, tooltip, category, optional, dataClass, minArgs, maxArgs, allowDuplicates);
	}

	@Override
	protected void process(DataParam params, List<TestData> data) {
		params.setData(data);
	}

}
