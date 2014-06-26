package chiptools.jprobe.function.peakfilter;

import jprobe.services.function.Function;
import util.genome.peak.Peak;
import util.genome.peak.PeakUtils.Filter;
import chiptools.jprobe.function.ChiptoolsDoubleArg;

public class MaxPValArg extends ChiptoolsDoubleArg<PeakFilterParams>{

	public MaxPValArg(Function<?> parent, boolean optional) {
		super(parent.getClass(), MaxPValArg.class, "off", optional, 0, 0, Double.POSITIVE_INFINITY, 1);
	}

	@Override
	protected void process(PeakFilterParams params, Double value) {
		final double max = value;
		params.addFilter(new Filter(){

			@Override
			public boolean keep(Peak p) {
				return p.getPVal() <= max;
			}
			
		});
	}

}
