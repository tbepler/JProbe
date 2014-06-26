package chiptools.jprobe.function.peakfilter;

import java.util.ArrayList;
import java.util.Collection;

import jprobe.services.data.Data;
import jprobe.services.function.Argument;
import util.genome.peak.PeakGroup;
import util.genome.peak.PeakUtils;
import util.progress.ProgressListener;
import chiptools.jprobe.data.Peaks;
import chiptools.jprobe.function.AbstractChiptoolsFunction;
import chiptools.jprobe.function.args.PeaksArgument;

public class PeakFilter extends AbstractChiptoolsFunction<PeakFilterParams>{

	public PeakFilter() {
		super(PeakFilterParams.class);
	}

	@Override
	public Collection<Argument<? super PeakFilterParams>> getArguments() {
		Collection<Argument<? super PeakFilterParams>> args = new ArrayList<Argument<? super PeakFilterParams>>();
		args.add(new PeaksArgument(this, false));
		args.add(new IncludeChromArg(this, true));
		args.add(new ExcludeChromArg(this, true));
		args.add(new MinQValArg(this, true));
		args.add(new MaxQValArg(this, true));
		args.add(new MinPValArg(this, true));
		args.add(new MaxPValArg(this, true));
		
		return args;
	}

	@Override
	public Data execute(ProgressListener l, PeakFilterParams params) throws Exception {
		PeakGroup peaks = params.getPeaks().getPeaks();
		PeakGroup filtered = PeakUtils.filter(peaks, params);
		return new Peaks(filtered);
	}

}
