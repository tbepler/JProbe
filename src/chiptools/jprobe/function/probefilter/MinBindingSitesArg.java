package chiptools.jprobe.function.probefilter;

import jprobe.services.function.Function;
import util.genome.probe.Probe;
import util.genome.probe.ProbeUtils.Filter;
import chiptools.jprobe.function.ChiptoolsIntArg;

public class MinBindingSitesArg extends ChiptoolsIntArg<ProbeFilterParam>{

	public MinBindingSitesArg(Function<?> parent, boolean optional) {
		super(parent.getClass(), MinBindingSitesArg.class, "off", optional, 0, 0, Integer.MAX_VALUE, 1);
	}

	@Override
	protected void process(ProbeFilterParam params, Integer value) {
		final int min = value;
		params.addFilter(new Filter(){

			@Override
			public boolean keep(Probe p) {
				return p.numBindingSites() >= min;
			}
			
		});
	}

}
