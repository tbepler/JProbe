package chiptools.jprobe.function.mutationprofiler;

import chiptools.Constants;
import jprobe.services.function.FlagArgument;

public class RecursiveArg extends FlagArgument<MutationProfilerParams>{

	protected RecursiveArg() {
		super(
				Constants.getName(RecursiveArg.class),
				Constants.getDescription(RecursiveArg.class),
				Constants.getCategory(RecursiveArg.class),
				Constants.getFlag(RecursiveArg.class)
				);
	}

	@Override
	protected void process(MutationProfilerParams params, boolean flagSet) {
		params.recursive = flagSet;
	}

}
