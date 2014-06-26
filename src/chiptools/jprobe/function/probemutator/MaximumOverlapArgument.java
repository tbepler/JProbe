package chiptools.jprobe.function.probemutator;

import jprobe.services.function.Function;
import chiptools.jprobe.function.ChiptoolsDoubleArg;

public class MaximumOverlapArgument extends ChiptoolsDoubleArg<ProbeMutatorParams>{

	public MaximumOverlapArgument(Function<?> parent, boolean optional) {
		super(
				parent.getClass(),
				MaximumOverlapArgument.class,
				optional,
				0.5,
				0,
				1,
				0.1
				);
	}

	@Override
	protected void process(ProbeMutatorParams params, Double value) {
		params.MAXIMUM_OVERLAP = value;
	}

}
