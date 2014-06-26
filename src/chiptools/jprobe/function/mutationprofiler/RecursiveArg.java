package chiptools.jprobe.function.mutationprofiler;

import chiptools.jprobe.function.ChiptoolsFlagArg;
import jprobe.services.function.Function;

public class RecursiveArg extends ChiptoolsFlagArg<MutationProfilerParams>{

	public RecursiveArg(Function<?> parent) {
		super(parent.getClass(), RecursiveArg.class);
	}

	@Override
	protected void process(MutationProfilerParams params, boolean flagSet) {
		params.recursive = flagSet;
	}

}
