package chiptools.jprobe.function.probejoiner;

import chiptools.Constants;
import jprobe.services.function.IntArgument;

public class MaxSiteDistArgument extends IntArgument<ProbeJoinerParams>{

	public MaxSiteDistArgument(boolean optional) {
		super(
				Constants.getName(MaxSiteDistArgument.class),
				Constants.getDescription(MaxSiteDistArgument.class),
				Constants.getCategory(MaxSiteDistArgument.class),
				Constants.getFlag(MaxSiteDistArgument.class),
				Constants.getPrototypeValue(MaxSiteDistArgument.class),
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
