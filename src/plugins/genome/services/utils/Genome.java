package plugins.genome.services.utils;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

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
	
	private final Comparator<GenomicCoordinate> LOC_ASCENDING = new Comparator<GenomicCoordinate>(){

		@Override
		public int compare(GenomicCoordinate o1, GenomicCoordinate o2) {
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
	
	private final Comparator<GenomicCoordinate> LOC_DESCENDING = new Comparator<GenomicCoordinate>(){

		@Override
		public int compare(GenomicCoordinate o1, GenomicCoordinate o2) {
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
			Comparator<GenomicCoordinate> locComp = getLocationAscendingComparator();
			if(o1.getStart().equals(o2.getStart())){
				return locComp.compare(o1.getEnd(), o2.getEnd());
			}
			return locComp.compare(o1.getStart(), o2.getStart());
		}
		
	};
	
	private final Comparator<GenomicRegion> REGION_START_DESCENDING = new Comparator<GenomicRegion>(){

		@Override
		public int compare(GenomicRegion o1, GenomicRegion o2) {
			Comparator<GenomicCoordinate> locComp = getLocationDescendingComparator();
			if(o1.getStart().equals(o2.getStart())){
				return locComp.compare(o1.getEnd(), o2.getEnd());
			}
			return locComp.compare(o1.getStart(), o2.getStart());
		}
		
	};
	
	private final Comparator<GenomicRegion> REGION_END_ASCENDING = new Comparator<GenomicRegion>(){

		@Override
		public int compare(GenomicRegion o1, GenomicRegion o2) {
			Comparator<GenomicCoordinate> locComp = getLocationAscendingComparator();
			if(o1.getEnd().equals(o2.getEnd())){
				return locComp.compare(o1.getStart(), o2.getStart());
			}
			return locComp.compare(o1.getEnd(), o2.getEnd());
		}
	
	};
	
	private final Comparator<GenomicRegion> REGION_END_DESCENDING = new Comparator<GenomicRegion>(){

		@Override
		public int compare(GenomicRegion o1, GenomicRegion o2) {
			Comparator<GenomicCoordinate> locComp = getLocationDescendingComparator();
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
	private final String m_Name;

	public Genome(String name,  Scanner genome){
		m_Name = name;
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
		return CHR_ASCENDING;
	}

	@Override
	public Comparator<Chromosome> getChrDescendingComparator() {
		return CHR_DESCENDING;
	}

	@Override
	public Comparator<GenomicCoordinate> getLocationAscendingComparator() {
		return LOC_ASCENDING;
	}

	@Override
	public Comparator<GenomicCoordinate> getLocationDescendingComparator() {
		return LOC_DESCENDING;
	}

	@Override
	public Comparator<GenomicRegion> getStartAscendingComparator() {
		return REGION_START_ASCENDING;
	}

	@Override
	public Comparator<GenomicRegion> getStartDescendingComparator() {
		return REGION_START_DESCENDING;
	}

	@Override
	public Comparator<GenomicRegion> getEndAscendingComparator() {
		return REGION_END_ASCENDING;
	}

	@Override
	public Comparator<GenomicRegion> getEndDescendingComparator() {
		return REGION_END_DESCENDING;
	}

	@Override
	public Chromosome getFirstChr() {
		return m_Chrs.getFirst();
	}

	@Override
	public Chromosome getLastChr() {
		return m_Chrs.getLast();
	}

	@Override
	public List<Chromosome> getChrs() {
		return Collections.unmodifiableList(m_Chrs);
	}
	
	@Override
	public boolean hasChr(Chromosome chr){
		return m_ChrPriority.containsKey(chr);
	}

	@Override
	public boolean hasChr(String id) {
		for(Chromosome chr : m_Chrs){
			if(chr.getId().equals(id)){
				return true;
			}
		}
		return false;
	}

	@Override
	public Chromosome getChr(String id) {
		for(Chromosome chr : m_Chrs){
			if(chr.getId().equals(id)){
				return chr;
			}
		}
		throw new RuntimeException("Error: no chromosome with id \""+id+"\" in genome "+this);
	}

	@Override
	public GenomicCoordinate newGenomicLocation(Chromosome chr, long baseIndex) {
		return new GenomicCoordinate(this, chr, baseIndex);
	}
	
	public GenomicCoordinate parseLocation(String s) throws ParsingException{
		return GenomicCoordinate.parseString(this, s);
	}

	@Override
	public GenomicRegion newGenomicRegion(GenomicCoordinate start, GenomicCoordinate end) {
		return new GenomicRegion(this, start, end);
	}
	
	public GenomicRegion parseRegion(String s) throws ParsingException{
		return GenomicRegion.parseString(this, s);
	}

	@Override
	public Chromosome nextChr(Chromosome cur) {
		return m_NextChr.get(cur);
	}

	@Override
	public Chromosome prevChr(Chromosome cur) {
		return m_PrevChr.get(cur);
	}
	
	@Override
	public String toString(){
		return m_Name;
	}
	
}
