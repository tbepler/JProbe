package util.genome.peak;

import java.util.List;

import util.genome.Genome;
import util.genome.GenomicSequence;

public class PeakQuery extends AbstractPeakQuery{
	private static final long serialVersionUID = 1L;

	private final List<PeakSequence> m_PeakSeqs;
	
	public PeakQuery(Peak peak, Genome genome, List<PeakSequence> peakSeqs) {
		super(peak, genome);
		m_PeakSeqs = peakSeqs;
	}

	@Override
	public void process(GenomicSequence sequence) {
		m_PeakSeqs.add(new PeakSequence(sequence, this.getPeak()));
	}

}
