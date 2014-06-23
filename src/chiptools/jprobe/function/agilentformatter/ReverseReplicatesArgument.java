package chiptools.jprobe.function.agilentformatter;

import chiptools.jprobe.function.ChiptoolsIntArg;

public class ReverseReplicatesArgument extends ChiptoolsIntArg<AgilentFormatterParams>{

	public ReverseReplicatesArgument(boolean optional) {
		super(
				ReverseReplicatesArgument.class,
				optional,
				3,
				0,
				Integer.MAX_VALUE,
				1
				);
	}

	@Override
	protected void process(AgilentFormatterParams params, Integer value) {
		params.RVS_REPS = value;
	}

}
