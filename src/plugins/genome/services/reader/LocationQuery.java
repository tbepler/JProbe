package plugins.genome.services.reader;

import java.io.Serializable;

import plugins.genome.services.utils.Chromosome;
import plugins.genome.services.utils.GenomicLocation;
import plugins.genome.services.utils.GenomicRegion;

public abstract class LocationQuery implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private final GenomicRegion m_Region;
	
	protected LocationQuery(GenomicRegion region){
		m_Region = region;
	}
	
	protected LocationQuery(GenomicLocation start, GenomicLocation end){
		this(new GenomicRegion(start, end));
	}
	
	protected LocationQuery(Chromosome startChr, long startBase, Chromosome endChr, long endBase){
		this(new GenomicLocation(startChr, startBase), new GenomicLocation(endChr, endBase));
	}
	
	protected LocationQuery(Chromosome startChr, int startBase, Chromosome endChr, int endBase){
		this(startChr, (long) startBase, endChr, (long) endBase);
	}
	
	protected LocationQuery(Chromosome chr, long start, long end){
		this(chr, start, chr, end);
	}
	
	protected LocationQuery(Chromosome chr, int start, int end){
		this(chr, (long) start, (long) end);
	}
	
	public GenomicRegion getRegion(){
		return m_Region;
	}
	
	public GenomicLocation getStart(){
		return m_Region.getStart();
	}
	
	public GenomicLocation getEnd(){
		return m_Region.getEnd();
	}
	
	public abstract void process(String locationSequence);
	
}
