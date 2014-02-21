package util.genome.peak;

import util.genome.Genome;
import util.genome.GenomicCoordinate;
import util.genome.GenomicRegion;
import util.genome.reader.query.LocationQuery;

public abstract class AbstractPeakQuery extends LocationQuery{
	private static final long serialVersionUID = 1L;
	
	private final Peak m_Peak;
	
	public AbstractPeakQuery(Peak peak, Genome genome) {
		super(generateQueryRegion(peak, genome));
		m_Peak = peak;
	}
	
	private static GenomicRegion generateQueryRegion(Peak peak, Genome genome){
		GenomicCoordinate start = genome.newGenomicCoordinate(peak.getChrom().getId(), peak.getChromStart());
		GenomicCoordinate end = genome.newGenomicCoordinate(peak.getChrom().getId(), peak.getChromEnd());
		return genome.newGenomicRegion(start, end);
	}
	
	public Peak getPeak(){
		return m_Peak;
	}
	
}
