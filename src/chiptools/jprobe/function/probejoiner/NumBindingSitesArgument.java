package chiptools.jprobe.function.probejoiner;

import chiptools.jprobe.function.ChiptoolsIntArg;
import jprobe.services.function.Function;

public class NumBindingSitesArgument extends ChiptoolsIntArg<ProbeJoinerParams>{

	public NumBindingSitesArgument(Function<?> parent, boolean optional) {
		super(
				parent.getClass(),
				NumBindingSitesArgument.class,
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
