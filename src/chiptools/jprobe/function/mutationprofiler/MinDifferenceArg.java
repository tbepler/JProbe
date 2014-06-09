package chiptools.jprobe.function.mutationprofiler;

import chiptools.jprobe.function.ChiptoolsDoubleArg;

public class MinDifferenceArg extends ChiptoolsDoubleArg<MutationProfilerParams>{

	protected MinDifferenceArg(boolean optional) {
		super(MinDifferenceArg.class, "off", optional, 0.1, 0, 1.0, 0.05);
	}

	@Override
	protected void process(MutationProfilerParams params, Double value) {
		params.minDifference = value;
	}

}
