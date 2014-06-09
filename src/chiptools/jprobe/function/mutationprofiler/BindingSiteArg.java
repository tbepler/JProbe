package chiptools.jprobe.function.mutationprofiler;

import chiptools.jprobe.function.ChiptoolsIntArg;

public class BindingSiteArg extends ChiptoolsIntArg<MutationProfilerParams>{

	public BindingSiteArg(boolean optional) {
		super(BindingSiteArg.class, "off", optional, 1, 1, Integer.MAX_VALUE, 1);
	}

	@Override
	protected void process(MutationProfilerParams params, Integer value) {
		params.bindingSite = value;
	}

}