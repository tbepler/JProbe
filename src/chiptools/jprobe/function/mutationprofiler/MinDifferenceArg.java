package chiptools.jprobe.function.mutationprofiler;

import jprobe.services.function.Function;
import chiptools.jprobe.function.ChiptoolsDoubleArg;

public class MinDifferenceArg extends ChiptoolsDoubleArg<MutationProfilerParams>{

	public MinDifferenceArg(Function<?> parent, boolean optional) {
		super(parent.getClass(), MinDifferenceArg.class, "off", optional, 0.1, 0, 1.0, 0.05);
	}

	@Override
	protected void process(MutationProfilerParams params, Double value) {
		params.minDifference = value;
	}

}
