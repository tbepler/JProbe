package chiptools.jprobe.function.args;

import chiptools.Constants;
import chiptools.jprobe.function.params.ProbeLenParam;
import jprobe.services.function.IntArgument;

public class ProbeLengthArgument extends IntArgument<ProbeLenParam>{

	public ProbeLengthArgument(boolean optional) {
		super(
				Constants.getName(ProbeLengthArgument.class),
				Constants.getDescription(ProbeLengthArgument.class) + ", default="+36,
				Constants.getCategory(ProbeLengthArgument.class),
				Constants.getFlag(ProbeLengthArgument.class),
				Constants.getPrototypeValue(ProbeLengthArgument.class) + "{default="+36+"}",
				optional,
				36,
				1,
				Integer.MAX_VALUE,
				1
				);
	}
	
	public ProbeLengthArgument(boolean optional, String defaultVal) {
		super(
				Constants.getName(ProbeLengthArgument.class),
				Constants.getDescription(ProbeLengthArgument.class) + ", default="+defaultVal,
				Constants.getCategory(ProbeLengthArgument.class),
				Constants.getFlag(ProbeLengthArgument.class),
				Constants.getPrototypeValue(ProbeLengthArgument.class) + "{default="+defaultVal+"}",
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
