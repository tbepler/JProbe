package chiptools.jprobe.function.probemutator;

import chiptools.jprobe.function.ChiptoolsFlagArg;
import jprobe.services.function.Function;

public class MutateBindingSitesArgument extends ChiptoolsFlagArg<ProbeMutatorParams>{

	protected MutateBindingSitesArgument(Function<?> parent) {
		super(
				parent.getClass(), MutateBindingSitesArgument.class
				);
	}

	@Override
	protected void process(ProbeMutatorParams params, boolean flagSet) {
		params.MUTATE_BINDING_SITES = flagSet;
	}

}
