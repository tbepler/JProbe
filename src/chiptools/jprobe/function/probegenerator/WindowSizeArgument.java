package chiptools.jprobe.function.probegenerator;

import chiptools.jprobe.function.ChiptoolsIntArg;
import jprobe.services.function.Function;

public class WindowSizeArgument extends ChiptoolsIntArg<ProbeGeneratorParams>{

	public WindowSizeArgument(Function<?> parent, boolean optional) {
		super(
				parent.getClass(),
				WindowSizeArgument.class,
				optional,
				3,
				0,
				Integer.MAX_VALUE,
				1
				);
	}

	@Override
	protected void process(ProbeGeneratorParams params, Integer value) {
		params.WINDOWSIZE = value;
	}

}
