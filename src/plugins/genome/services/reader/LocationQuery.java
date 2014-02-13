package plugins.genome.services.reader;

import java.io.Serializable;

import plugins.genome.services.utils.GenomicCoordinate;
import plugins.genome.services.utils.GenomicRegion;

public abstract class LocationQuery implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private final GenomicRegion m_Region;
	
	protected LocationQuery(GenomicRegion region){
		m_Region = region;
	}
	
	public GenomicRegion getRegion(){
		return m_Region;
	}
	
	public GenomicCoordinate getStart(){
		return m_Region.getStart();
	}
	
	public GenomicCoordinate getEnd(){
		return m_Region.getEnd();
	}
	
	public abstract void process(String locationSequence);
	
}
