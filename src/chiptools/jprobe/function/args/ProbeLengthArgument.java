package chiptools.jprobe.function.args;

import chiptools.Constants;
import chiptools.jprobe.function.params.ProbeLenParam;
import jprobe.services.function.IntArgument;

public class ProbeLengthArgument extends IntArgument<ProbeLenParam>{

	public ProbeLengthArgument(boolean optional) {
		super(
				Constants.getName(ProbeLengthArgument.class),
				Constants.getDescription(ProbeLengthArgument.class),
				Constants.getCategory(ProbeLengthArgument.class),
				Constants.getFlag(ProbeLengthArgument.class),
				Constants.getPrototypeValue(ProbeLengthArgument.class),
				optional,
				36,
				1,
				Integer.MAX_VALUE,
				1
				);
	}

	@Override
	protected void process(ProbeLenParam params, Integer value) {
		params.setProbeLength(value);
	}

}
