package chiptools.jprobe.function.probefilter;

import util.genome.probe.Probe;
import util.genome.probe.ProbeUtils.Filter;
import chiptools.jprobe.function.ChiptoolsIntArg;

public class MaxMutationsArgument extends ChiptoolsIntArg<ProbeFilterParam>{

	protected MaxMutationsArgument(boolean optional) {
		super(MaxMutationsArgument.class, "off", optional, 0, 0, Integer.MAX_VALUE, 1);
	}

	@Override
	protected void process(ProbeFilterParam params, Integer value) {
		final int max = value;
		params.addFilter(new Filter(){

			@Override
			public boolean keep(Probe p) {
				return p.numMutations() <= max;
			}
			
		});
	}

}
