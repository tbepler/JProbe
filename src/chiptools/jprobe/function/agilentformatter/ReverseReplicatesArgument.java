package chiptools.jprobe.function.agilentformatter;

import jprobe.services.function.Function;
import chiptools.jprobe.function.ChiptoolsIntArg;

public class ReverseReplicatesArgument extends ChiptoolsIntArg<AgilentFormatterParams>{

	public ReverseReplicatesArgument(Function<?> parent, boolean optional) {
		super(
				parent.getClass(),
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
