package chiptools.jprobe.function.probemutator;

import chiptools.jprobe.function.ChiptoolsIntArg;
import jprobe.services.function.Function;

public class BindingBarrierArgument extends ChiptoolsIntArg<ProbeMutatorParams>{

	public BindingBarrierArgument(Function<?> parent, boolean optional) {
		super(
				parent.getClass(),
				BindingBarrierArgument.class,
				optional,
				2,
				0,
				Integer.MAX_VALUE,
				1
				);
	}

	@Override
	protected void process(ProbeMutatorParams params, Integer value) {
		params.BINDING_SITE_BARRIER = value;
	}

}
