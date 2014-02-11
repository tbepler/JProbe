package plugins.genome.services.utils;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;

public interface GenomicContext extends Serializable{
	
	public Comparator<Chromosome> getChrAscendingComparator();
	public Comparator<Chromosome> getChrDescendingComparator();
	
	public Comparator<GenomicLocation> getLocationAscendingComparator();
	public Comparator<GenomicLocation> getLocationDescendingComparator();
	
	public Comparator<GenomicRegion> getStartAscendingComparator();
	public Comparator<GenomicRegion> getStartDescendingComparator();
	public Comparator<GenomicRegion> getEndAscendingComparator();
	public Comparator<GenomicRegion> getEndDescendingComparator();
	
	public Chromosome getFirstChr();
	public Chromosome getLastChr();
	public List<Chromosome> getChrs();
	public boolean hasChr(String id);
	public Chromosome getChr(String id);
	
	public Chromosome nextChr(Chromosome cur);
	public Chromosome prevChr(Chromosome cur);
	
	public GenomicLocation newGenomicLocation(Chromosome chr, long baseIndex);
	public GenomicRegion newGenomicRegion(GenomicLocation start, GenomicLocation end);
	
}
