package chiptools.jprobe.function.peakfilter;

import java.util.Collection;

import jprobe.services.data.Data;
import jprobe.services.function.Argument;
import util.progress.ProgressListener;
import chiptools.jprobe.function.AbstractChiptoolsFunction;

public class PeakFilter extends AbstractChiptoolsFunction<PeakFilterParams>{

	protected PeakFilter() {
		super(PeakFilterParams.class);
	}

	@Override
	public Collection<Argument<? super PeakFilterParams>> getArguments() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Data execute(ProgressListener l, PeakFilterParams params)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
