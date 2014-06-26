package chiptools.jprobe.function.peakfilter;

import jprobe.services.function.Function;
import util.genome.peak.Peak;
import util.genome.peak.PeakUtils.Filter;
import chiptools.jprobe.function.ChiptoolsDoubleArg;

public class MinPValArg extends ChiptoolsDoubleArg<PeakFilterParams>{

	public MinPValArg(Function<?> parent, boolean optional) {
		super(parent.getClass(), MinPValArg.class, "off", optional, 0, 0, Double.POSITIVE_INFINITY, 1.0);
	}

	@Override
	protected void process(PeakFilterParams params, Double value) {
		final double min = value;
		params.addFilter(new Filter(){

			@Override
			public boolean keep(Peak p) {
				return p.getPVal() >= min;
			}
			
		});
	}

}
