package plugins.genome.services.reader;

import plugins.genome.services.utils.GenomicLocation;
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
	
	public GenomicLocation getStart(){
		return m_Region.getStart();
	}
	
	public GenomicLocation getEnd(){
		return m_Region.getEnd();
	}

}
