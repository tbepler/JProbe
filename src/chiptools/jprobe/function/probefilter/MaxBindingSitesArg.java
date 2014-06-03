package chiptools.jprobe.function.probefilter;

import util.genome.probe.Probe;
import util.genome.probe.ProbeUtils.Filter;
import chiptools.jprobe.function.ChiptoolsIntArg;

public class MaxBindingSitesArg extends ChiptoolsIntArg<ProbeFilterParam>{

	public MaxBindingSitesArg(boolean optional) {
		super(MaxBindingSitesArg.class, "off", optional, 0, 0, Integer.MAX_VALUE, 1);
	}

	@Override
	protected void process(ProbeFilterParam params, Integer value) {
		final int max = value;
		params.addFilter(new Filter(){

			@Override
			public boolean keep(Probe p) {
				return p.numBindingSites() <= max;
			}
			
		});
	}

}
