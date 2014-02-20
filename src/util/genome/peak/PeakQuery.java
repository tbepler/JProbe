package util.genome.peak;

import java.util.List;

import util.genome.Genome;
import util.genome.GenomicCoordinate;
import util.genome.GenomicRegion;
import util.genome.GenomicSequence;
import util.genome.reader.query.LocationQuery;

public class PeakQuery extends LocationQuery{
	private static final long serialVersionUID = 1L;
	
	private final Peak m_Peak;
	private final List<PeakSequence> m_PeakSeqs;
	
	public PeakQuery(Peak peak, Genome genome, List<PeakSequence> peakSeqs) {
		super(generateQueryRegion(peak, genome));
		m_Peak = peak;
		m_PeakSeqs = peakSeqs;
	}
	
	private static GenomicRegion generateQueryRegion(Peak peak, Genome genome){
		GenomicCoordinate start = genome.newGenomicCoordinate(peak.getChrom().getId(), peak.getChromStart());
		GenomicCoordinate end = genome.newGenomicCoordinate(peak.getChrom().getId(), peak.getChromEnd());
		return genome.newGenomicRegion(start, end);
	}

	@Override
	public void process(GenomicSequence sequence) {
		m_PeakSeqs.add(new PeakSequence(sequence, m_Peak));
	}

}
