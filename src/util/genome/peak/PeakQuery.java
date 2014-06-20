package util.genome.peak;

import java.util.List;

import util.genome.GenomicSequence;
import util.genome.Strand;

public class PeakQuery extends AbstractPeakQuery{
	private static final long serialVersionUID = 1L;

	private final List<PeakSequence> m_PeakSeqs;
	
	public PeakQuery(Peak peak, List<PeakSequence> peakSeqs) {
		super(peak);
		m_PeakSeqs = peakSeqs;
	}

	@Override
	public void process(GenomicSequence sequence) {
		GenomicSequence upperCased = new GenomicSequence(sequence.getSequence().toUpperCase(), sequence.getRegion());
		m_PeakSeqs.add(new PeakSequence(upperCased, this.getPeak(), Strand.PLUS));
	}

}
