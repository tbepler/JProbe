package chiptools.jprobe.function.peakfilter;

import chiptools.jprobe.function.ChiptoolsDoubleArg;

public class MinQValArg extends ChiptoolsDoubleArg<PeakFilterParams>{

	public MinQValArg(boolean optional) {
		super(MinQValArg.class, "off", optional, 0, 0, Double.POSITIVE_INFINITY, 1.0);
	}

	@Override
	protected void process(PeakFilterParams params, Double value) {
		// TODO Auto-generated method stub
		
	}

}
