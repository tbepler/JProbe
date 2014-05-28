package chiptools.jprobe.function.probejoiner;

import chiptools.Constants;
import jprobe.services.function.IntArgument;

public class MinSiteDistArgument extends IntArgument<ProbeJoinerParams>{

	public MinSiteDistArgument(boolean optional) {
		super(
				Constants.getName(MinSiteDistArgument.class),
				Constants.getDescription(MinSiteDistArgument.class),
				Constants.getCategory(MinSiteDistArgument.class),
				Constants.getFlag(MinSiteDistArgument.class),
				Constants.getPrototypeValue(MinSiteDistArgument.class),
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
