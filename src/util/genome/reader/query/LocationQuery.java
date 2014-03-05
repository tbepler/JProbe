package util.genome.reader.query;

import java.io.Serializable;
import java.util.Comparator;

import util.genome.Chromosome;
import util.genome.GenomicCoordinate;
import util.genome.GenomicRegion;
import util.genome.GenomicSequence;

public abstract class LocationQuery implements Serializable{
	private static final long serialVersionUID = 1L;
	
	public static final Comparator<LocationQuery> START_COMPARATOR = new Comparator<LocationQuery>(){

		@Override
		public int compare(LocationQuery arg0, LocationQuery arg1) {
			if(arg0.getStart().equals(arg1.getStart())){
				return arg0.getEnd().compareTo(arg1.getEnd());
			}
			return arg0.getStart().compareTo(arg1.getStart());
		}
		
	};
	
	public static final Comparator<LocationQuery> END_COMPARATOR = new Comparator<LocationQuery>(){

		@Override
		public int compare(LocationQuery o1, LocationQuery o2) {
			if(o1.getEnd().equals(o2.getEnd())){
				return o1.getStart().compareTo(o2.getStart());
			}
			return o1.getEnd().compareTo(o2.getEnd());
		}
		
	};
	
	private final GenomicRegion m_Region;
	
	protected LocationQuery(GenomicRegion region){
		m_Region = region;
	}
	
	public Chromosome getChromosome(){
		return m_Region.getChromosome();
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
	
	public abstract void process(GenomicSequence found);
	
}
