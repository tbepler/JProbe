package util.genome.peak;

import util.genome.GenomicCoordinate;
import util.genome.GenomicRegion;
import util.genome.reader.query.LocationQuery;

public abstract class AbstractPeakQuery extends LocationQuery{
	private static final long serialVersionUID = 1L;
	
	private final Peak m_Peak;
	
	public AbstractPeakQuery(Peak peak) {
		super(generateQueryRegion(peak));
		m_Peak = peak;
	}
	
	private static GenomicRegion generateQueryRegion(Peak peak){
		GenomicCoordinate start = new GenomicCoordinate(peak.getChrom().getId(), peak.getChromStart());
		GenomicCoordinate end = new GenomicCoordinate(peak.getChrom().getId(), peak.getChromEnd());
		return new GenomicRegion(start, end);
	}
	
	public Peak getPeak(){
		return m_Peak;
	}
	
}
