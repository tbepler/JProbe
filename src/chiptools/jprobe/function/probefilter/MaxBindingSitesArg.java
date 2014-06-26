package chiptools.jprobe.function.probefilter;

import jprobe.services.function.Function;
import util.genome.probe.Probe;
import util.genome.probe.ProbeUtils.Filter;
import chiptools.jprobe.function.ChiptoolsIntArg;

public class MaxBindingSitesArg extends ChiptoolsIntArg<ProbeFilterParam>{

	public MaxBindingSitesArg(Function<?> parent, boolean optional) {
		super(parent.getClass(), MaxBindingSitesArg.class, "off", optional, 0, 0, Integer.MAX_VALUE, 1);
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
