package chiptools.jprobe.function;

import java.util.ArrayList;
import java.util.Collection;

import util.genome.peak.PeakGroup;
import util.genome.peak.PeakSequenceGroup;
import util.genome.reader.GenomeReader;
import util.genome.reader.GenomeReaderFactory;
import util.progress.ProgressListener;
import chiptools.jprobe.ChiptoolsActivator;
import chiptools.jprobe.data.PeakSequences;
import chiptools.jprobe.function.params.GenomeArgument;
import chiptools.jprobe.function.params.GenomePeakFinderParams;
import chiptools.jprobe.function.params.PeaksArgument;
import jprobe.services.data.Data;
import jprobe.services.function.Argument;

public class GenomePeakFinder extends AbstractChiptoolsFunction<GenomePeakFinderParams>{

	protected GenomePeakFinder() {
		super(GenomePeakFinderParams.class);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Collection<Argument<? super GenomePeakFinderParams>> getArguments() {
		Collection<Argument<? super GenomePeakFinderParams>> args = new ArrayList<Argument<? super GenomePeakFinderParams>>();
		args.add(new PeaksArgument(ChiptoolsActivator.getCore(), false));
		args.add(new GenomeArgument(false));
		return args;
	}

	@Override
	public Data execute(ProgressListener l, GenomePeakFinderParams params) throws Exception {
		PeakGroup peaks = params.getPeaks().getPeaks();
		Collection<ProgressListener> listeners = new ArrayList<ProgressListener>();
		listeners.add(l);
		GenomeReader genomeReader = GenomeReaderFactory.createGenomeReader(params.getGenomeFile(), listeners);
		return new PeakSequences(PeakSequenceGroup.readFromGenome(genomeReader, peaks));
	}

}
