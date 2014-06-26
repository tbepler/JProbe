package chiptools.jprobe.function.agilentformatter;

import jprobe.services.function.Function;
import chiptools.jprobe.function.ChiptoolsIntArg;

public class ForwardReplicatesArgument extends ChiptoolsIntArg<AgilentFormatterParams>{

	public ForwardReplicatesArgument(Function<?> parent, boolean optional) {
		super(
				parent.getClass(),
				ForwardReplicatesArgument.class,
				optional,
				3,
				0,
				Integer.MAX_VALUE,
				1
				);
	}

	@Override
	protected void process(AgilentFormatterParams params, Integer value) {
		params.FWD_REPS = value;
	}

}
