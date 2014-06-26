package chiptools.jprobe.function.probejoiner;

import chiptools.jprobe.function.ChiptoolsIntArg;
import jprobe.services.function.Function;

public class MaxSiteDistArgument extends ChiptoolsIntArg<ProbeJoinerParams>{

	public MaxSiteDistArgument(Function<?> parent, boolean optional) {
		super(
				parent.getClass(),
				MaxSiteDistArgument.class,
				optional,
				16,
				0,
				Integer.MAX_VALUE,
				1
				);
	}

	@Override
	protected void process(ProbeJoinerParams params, Integer value) {
		params.MAXSITEDIST = value;
	}

}
