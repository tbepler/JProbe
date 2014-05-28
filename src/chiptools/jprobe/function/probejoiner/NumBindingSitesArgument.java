package chiptools.jprobe.function.probejoiner;

import chiptools.Constants;
import jprobe.services.function.IntArgument;

public class NumBindingSitesArgument extends IntArgument<ProbeJoinerParams>{

	public NumBindingSitesArgument(boolean optional) {
		super(
				Constants.getName(NumBindingSitesArgument.class),
				Constants.getDescription(NumBindingSitesArgument.class),
				Constants.getCategory(NumBindingSitesArgument.class),
				Constants.getFlag(NumBindingSitesArgument.class),
				Constants.getPrototypeValue(NumBindingSitesArgument.class),
				optional,
				0,
				0,
				Integer.MAX_VALUE,
				1
				);
	}

	@Override
	protected void process(ProbeJoinerParams params, Integer value) {
		params.NUMBINDINGSITES = value;
	}

}
