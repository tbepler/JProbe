package chiptools.jprobe.function.args;

import chiptools.Constants;
import chiptools.jprobe.function.params.ProbeGeneratorParams;
import jprobe.services.function.IntArgument;

public class ProbeLengthArgument extends IntArgument<ProbeGeneratorParams>{

	public ProbeLengthArgument(boolean optional) {
		super(
				Constants.getName(ProbeLengthArgument.class),
				Constants.getDescription(ProbeLengthArgument.class),
				Constants.getCategory(ProbeLengthArgument.class),
				optional,
				36,
				1,
				Integer.MAX_VALUE,
				1
				);
	}

	@Override
	protected void process(ProbeGeneratorParams params, Integer value) {
		params.PROBELEN = value;
	}

}
