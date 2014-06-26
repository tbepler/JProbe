package chiptools.jprobe.function.probejoiner;

import chiptools.jprobe.function.ChiptoolsIntArg;
import jprobe.services.function.Function;

public class MinSiteDistArgument extends ChiptoolsIntArg<ProbeJoinerParams>{

	public MinSiteDistArgument(Function<?> parent, boolean optional) {
		super(
				parent.getClass(),
				MinSiteDistArgument.class,
				optional,
				2,
				0,
				Integer.MAX_VALUE,
				1
				);
	}

	@Override
	protected void process(ProbeJoinerParams params, Integer value) {
		params.MINSITEDIST = value;
	}

}
