package plugins.genome.services.reader;

import plugins.genome.services.utils.GenomicCoordinate;
import plugins.genome.services.utils.GenomicRegion;

public abstract class LocationBoundedSequenceQuery extends SequenceQuery{
	private static final long serialVersionUID = 1L;
	
	private final GenomicRegion m_Region;

	protected LocationBoundedSequenceQuery(String targetSequence, GenomicRegion searchRegion) {
		super(targetSequence);
		m_Region = searchRegion;
	}
	
	public GenomicRegion getSearchRegion(){
		return m_Region;
	}
	
	public GenomicCoordinate getStart(){
		return m_Region.getStart();
	}
	
	public GenomicCoordinate getEnd(){
		return m_Region.getEnd();
	}

}
