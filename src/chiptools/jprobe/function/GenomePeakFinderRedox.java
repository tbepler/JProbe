package chiptools.jprobe.function;

import java.util.Collection;

import util.progress.ProgressListener;
import chiptools.jprobe.function.params.GenomePeakFinderParams;
import jprobe.services.data.Data;
import jprobe.services.function.Argument;

public class GenomePeakFinderRedox extends AbstractChiptoolsFunction<GenomePeakFinderParams>{

	protected GenomePeakFinderRedox() {
		super(GenomePeakFinderParams.class);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Collection<Argument<? super GenomePeakFinderParams>> getArguments() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Data execute(ProgressListener l, GenomePeakFinderParams params)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
