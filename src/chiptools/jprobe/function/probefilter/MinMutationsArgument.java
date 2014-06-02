package chiptools.jprobe.function.probefilter;

import util.genome.probe.Probe;
import util.genome.probe.ProbeUtils.Filter;
import chiptools.jprobe.function.ChiptoolsIntArg;

public class MinMutationsArgument extends ChiptoolsIntArg<ProbeFilterParam>{


	protected MinMutationsArgument(boolean optional) {
		super(
				MinMutationsArgument.class,
				"off",
				optional,
				0,
				0,
				Integer.MAX_VALUE,
				1
				);
	}

	@Override
	protected void process(ProbeFilterParam params, Integer value) {
		final int min = value;
		params.addFilter(new Filter(){

			@Override
			public boolean keep(Probe p) {
				return p.numMutations() >= min;
			}
			
		});
	}

}
