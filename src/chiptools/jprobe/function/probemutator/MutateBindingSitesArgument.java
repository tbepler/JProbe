package chiptools.jprobe.function.probemutator;

import chiptools.Constants;
import jprobe.services.function.FlagArgument;

public class MutateBindingSitesArgument extends FlagArgument<ProbeMutatorParams>{

	protected MutateBindingSitesArgument() {
		super(
				Constants.getName(MutateBindingSitesArgument.class),
				Constants.getDescription(MutateBindingSitesArgument.class),
				Constants.getCategory(MutateBindingSitesArgument.class),
				Constants.getFlag(MutateBindingSitesArgument.class)
				);
	}

	@Override
	protected void process(ProbeMutatorParams params, boolean flagSet) {
		params.MUTATE_BINDING_SITES = flagSet;
	}

}
