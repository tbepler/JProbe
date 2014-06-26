package chiptools.jprobe.function.args;

import chiptools.jprobe.function.ChiptoolsIntArg;
import chiptools.jprobe.function.params.ProbeLenParam;
import jprobe.services.function.Function;

public class ProbeLengthArgument extends ChiptoolsIntArg<ProbeLenParam>{

	public ProbeLengthArgument(Function<?> parent, boolean optional) {
		super(
				parent.getClass(),
				ProbeLengthArgument.class,
				optional,
				36,
				1,
				Integer.MAX_VALUE,
				1
				);
	}
	
	public ProbeLengthArgument(Function<?> parent, boolean optional, String defaultVal) {
		super(
				parent.getClass(),
				ProbeLengthArgument.class,
				defaultVal,
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
