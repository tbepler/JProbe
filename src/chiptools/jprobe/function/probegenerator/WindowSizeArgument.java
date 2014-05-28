package chiptools.jprobe.function.probegenerator;

import chiptools.Constants;
import jprobe.services.function.IntArgument;

public class WindowSizeArgument extends IntArgument<ProbeGeneratorParams>{

	public WindowSizeArgument(boolean optional) {
		super(
				Constants.getName(WindowSizeArgument.class),
				Constants.getDescription(WindowSizeArgument.class),
				Constants.getCategory(WindowSizeArgument.class),
				Constants.getFlag(WindowSizeArgument.class),
				Constants.getPrototypeValue(WindowSizeArgument.class),
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
