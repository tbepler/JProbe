package plugins.genome.services.utils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import plugins.genome.ThreadedGenomeReader.LocationComparator;

public class Genome implements GenomicContext{
	private static final long serialVersionUID = 1L;
	
	private static final String CHR_LINE = "^((>[Cc]hr)|(>[Cc]hromosome)).+$";
	private static final String CHR_MARKER = "^((>[Cc]hr)|(>[Cc]hromosome))";
	
	private final Comparator<Chromosome> CHR_ASCENDING = new Comparator<Chromosome>(){

		@Override
		public int compare(Chromosome o1, Chromosome o2) {
			return m_ChrPriority.get(o1) - m_ChrPriority.get(o2);
		}
		
	};
	
	private final Comparator<Chromosome> CHR_DESCENDING = new Comparator<Chromosome>(){

		@Override
		public int compare(Chromosome o1, Chromosome o2) {
			return m_ChrPriority.get(o2) - m_ChrPriority.get(o1);
		}
		
	};
	
	private final Comparator<GenomicLocation> LOC_ASCENDING = new Comparator<GenomicLocation>(){

		@Override
		public int compare(GenomicLocation o1, GenomicLocation o2) {
			if(o1.getChromosome().equals(o2.getChromosome())){
				long base1 = o1.getBaseIndex();
				long base2 = o2.getBaseIndex();
				if(base1 < base2) return -1;
				if(base1 > base2) return 1;
				return 0;
			}
			return getChrAscendingComparator().compare(o1.getChromosome(), o2.getChromosome());
		}
		
	};
	
	private final Comparator<GenomicLocation> LOC_DESCENDING = new Comparator<GenomicLocation>(){

		@Override
		public int compare(GenomicLocation o1, GenomicLocation o2) {
			if(o1.getChromosome().equals(o2.getChromosome())){
				long base1 = o1.getBaseIndex();
				long base2 = o2.getBaseIndex();
				if(base1 < base2) return 1;
				if(base1 > base2) return -1;
				return 0;
			}
			return getChrDescendingComparator().compare(o1.getChromosome(), o2.getChromosome());
		}
		
	};
	
	private final Comparator<GenomicRegion> REGION_START_ASCENDING = new Comparator<GenomicRegion>(){

		@Override
		public int compare(GenomicRegion o1, GenomicRegion o2) {
			Comparator<GenomicLocation> locComp = getLocationAscendingComparator();
			if(o1.getStart().equals(o2.getStart())){
				return locComp.compare(o1.getEnd(), o2.getEnd());
			}
			return locComp.compare(o1.getStart(), o2.getStart());
		}
		
	};
	
	private final Comparator<GenomicRegion> REGION_START_DESCENDING = new Comparator<GenomicRegion>(){

		@Override
		public int compare(GenomicRegion o1, GenomicRegion o2) {
			Comparator<GenomicLocation> locComp = getLocationDescendingComparator();
			if(o1.getStart().equals(o2.getStart())){
				return locComp.compare(o1.getEnd(), o2.getEnd());
			}
			return locComp.compare(o1.getStart(), o2.getStart());
		}
		
	};
	
	private final Comparator<GenomicRegion> REGION_END_ASCENDING = new Comparator<GenomicRegion>(){

		@Override
		public int compare(GenomicRegion o1, GenomicRegion o2) {
			Comparator<GenomicLocation> locComp = getLocationAscendingComparator();
			if(o1.getEnd().equals(o2.getEnd())){
				return locComp.compare(o1.getStart(), o2.getStart());
			}
			return locComp.compare(o1.getEnd(), o2.getEnd());
		}
	
	};
	
	private final Comparator<GenomicRegion> REGION_END_DESCENDING = new Comparator<GenomicRegion>(){

		@Override
		public int compare(GenomicRegion o1, GenomicRegion o2) {
			Comparator<GenomicLocation> locComp = getLocationDescendingComparator();
			if(o1.getEnd().equals(o2.getEnd())){
				return locComp.compare(o1.getStart(), o2.getStart());
			}
			return locComp.compare(o1.getEnd(), o2.getEnd());
		}
		
	};
	
	private LinkedList<Chromosome> m_Chrs;
	private Map<Chromosome, Integer> m_ChrPriority;
	private Map<Chromosome, Chromosome> m_NextChr;
	private Map<Chromosome, Chromosome> m_PrevChr;

	public Genome(Scanner genome){
		m_Chrs = new LinkedList<Chromosome>();
		m_ChrPriority = new HashMap<Chromosome, Integer>();
		m_NextChr = new HashMap<Chromosome, Chromosome>();
		m_PrevChr = new HashMap<Chromosome, Chromosome>();
		String curTag = null;
		long count = 0;
		while(genome.hasNextLine()){
			String line = genome.nextLine();
			if(isChrMarker(line)){
				if(curTag != null){
					Chromosome chr = new Chromosome(this, curTag, count);
					m_Chrs.add(chr);
					m_ChrPriority.put(chr, m_Chrs.size());
				}
				curTag = line.replaceFirst(CHR_MARKER, "");
				count = 0;
			}else{
				count += line.length();
			}
		}
		Chromosome prev = null;
		for(Chromosome cur : m_Chrs){
			m_NextChr.put(prev, cur);
			m_PrevChr.put(cur, prev);
			prev = cur;
		}
		m_NextChr.put(prev, null);
	}
	
	private boolean isChrMarker(String line){
		return line.matches(CHR_LINE);
	}

	@Override
	public Comparator<Chromosome> getChrAscendingComparator() {
		return new Comparator<Chromosome>(){
			
			@Override
			public int compare(Chromosome o1, Chromosome o2) {
				return m_ChrPriority.get(o1) - m_ChrPriority.get(o2);
			}
			
		};
	}

	@Override
	public Comparator<Chromosome> getChrDescendingComparator() {
		return new Comparator<Chromosome>(){

			@Override
			public int compare(Chromosome o1, Chromosome o2) {
				return m_ChrPriority.get(o2) - m_ChrPriority.get(o1);
			}
			
		};
	}

	@Override
	public Comparator<GenomicLocation> getLocationAscendingComparator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Comparator<GenomicLocation> getLocationDescendingComparator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Comparator<GenomicRegion> getStartAscendingComparator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Comparator<GenomicRegion> getStartDescendingComparator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Comparator<GenomicRegion> getEndAscendingComparator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Comparator<GenomicRegion> getEndDescendingComparator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Chromosome getFirstChr() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Chromosome getLastChr() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Chromosome> getChrs() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasChr(String id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Chromosome getChr(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GenomicLocation newGenomicLocation(Chromosome chr, long baseIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GenomicRegion newGenomicRegion(GenomicLocation start,
			GenomicLocation end) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Chromosome nextChr(Chromosome cur) {
		return m_NextChr.get(cur);
	}

	@Override
	public Chromosome prevChr(Chromosome cur) {
		return m_PrevChr.get(cur);
	}
	
}
