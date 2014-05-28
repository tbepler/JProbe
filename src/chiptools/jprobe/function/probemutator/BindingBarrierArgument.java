package chiptools.jprobe.function.probemutator;

import chiptools.Constants;
import jprobe.services.function.IntArgument;

public class BindingBarrierArgument extends IntArgument<ProbeMutatorParams>{

	public BindingBarrierArgument(boolean optional) {
		super(
				Constants.getName(BindingBarrierArgument.class),
				Constants.getDescription(BindingBarrierArgument.class),
				Constants.getCategory(BindingBarrierArgument.class),
				Constants.getFlag(BindingBarrierArgument.class),
				Constants.getPrototypeValue(BindingBarrierArgument.class),
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
