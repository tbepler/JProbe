package plugins.genome;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.TreeMap;

import plugins.genome.ThreadedGenomeReader.LocationComparator;
import plugins.genome.services.reader.LocationBoundedSequenceQuery;
import plugins.genome.services.reader.LocationQuery;
import plugins.genome.services.reader.SequenceQuery;
import plugins.genome.services.utils.Chromosome;
import plugins.genome.services.utils.GenomicCoordinate;
import plugins.genome.services.utils.GenomicRegion;

public class GenomeWorker implements Runnable{
	
	private final SequenceProvider m_Provider;
	private final Queue<LocationQuery> m_LocationQueries;
	private final Queue<LocationBoundedSequenceQuery> m_BoundedQueries;
	private final List<SequenceQuery> m_SequenceQueries;
	private final Map<SequenceQuery, GenomicCoordinate> m_LastLocationSearched;
	private final Queue<LocationQuery> m_ActiveLocationQueries;
	private final Queue<LocationBoundedSequenceQuery> m_ActiveBoundedQueries;
	private final int m_MaxTargetLen;
	private final Map<Chromosome, Integer> m_ChrIndexes;
	private final LocationComparator m_Comparator;
	private Chromosome m_CurChr;
	private GenomicRegion m_CurSeqRegion;
	private String m_CurSeq;
	
	public GenomeWorker(SequenceProvider provider, Queue<LocationQuery> locationQueries, Queue<LocationBoundedSequenceQuery> boundedQueries,
			List<SequenceQuery> sequenceQueries, final LocationComparator genomeComp){
		m_Comparator = genomeComp;
		m_Provider = provider;
		m_LocationQueries = locationQueries;
		m_BoundedQueries = boundedQueries;
		m_SequenceQueries = sequenceQueries;
		m_LastLocationSearched = new HashMap<SequenceQuery, GenomicCoordinate>();
		m_ActiveLocationQueries = new PriorityQueue<LocationQuery>(10, new Comparator<LocationQuery>(){
			@Override
			public int compare(LocationQuery o1, LocationQuery o2) {
				return genomeComp.compare(o1.getEnd(), o2.getEnd());
			}
		});
		m_ActiveBoundedQueries = new PriorityQueue<LocationBoundedSequenceQuery>(10, new Comparator<LocationBoundedSequenceQuery>(){
			@Override
			public int compare(LocationBoundedSequenceQuery o1, LocationBoundedSequenceQuery o2) {
				return genomeComp.compare(o1.getEnd(), o2.getEnd());
			}
		});
		m_MaxTargetLen = this.findMaxTargetLength(sequenceQueries, boundedQueries);
		m_ChrIndexes = new HashMap<Chromosome, Integer>();
		m_CurChr = null;
		m_CurSeqRegion = null;
		m_CurSeq = "";
	}
	
	private int findMaxTargetLength(List<SequenceQuery> seqQueries, Queue<LocationBoundedSequenceQuery> boundedQueries){
		int max = 0;
		for(SequenceQuery q : seqQueries){
			if(q.getTargetSequence().length() > max){
				max = q.getTargetSequence().length();
			}
		}
		for(LocationBoundedSequenceQuery q : boundedQueries){
			if(q.getTargetSequence().length() > max){
				max = q.getTargetSequence().length();
			}
		}
		return max;
	}
	
	private boolean jobsRemaining(){
		return !m_LocationQueries.isEmpty() || !m_BoundedQueries.isEmpty() || !m_SequenceQueries.isEmpty() || !m_ActiveLocationQueries.isEmpty()
				|| !m_ActiveBoundedQueries.isEmpty();
	}
	
	private GenomicCoordinate getNextStartLocation(){
		if(m_LocationQueries.isEmpty() && m_BoundedQueries.isEmpty()){
			return null;
		}
		if(m_LocationQueries.isEmpty()){
			return m_BoundedQueries.peek().getStart();
		}
		if(m_BoundedQueries.isEmpty()){
			return m_LocationQueries.peek().getStart();
		}
		GenomicCoordinate a = m_LocationQueries.peek().getStart();
		GenomicCoordinate b = m_BoundedQueries.peek().getStart();
		return m_Comparator.compare(a, b) > 0 ? b : a;
	}
	
	private boolean regionContains(GenomicRegion region, GenomicCoordinate location){
		GenomicCoordinate start = region.getStart();
		GenomicCoordinate end = region.getEnd();
		return m_Comparator.compare(start, location) <= 0 && m_Comparator.compare(location, end) <= 0;
	}
	
	private boolean recordSequence(GenomicRegion nextRegion){
		return !m_SequenceQueries.isEmpty() || !m_ActiveLocationQueries.isEmpty() 
				|| !m_ActiveBoundedQueries.isEmpty() || this.regionContains(nextRegion, this.getNextStartLocation());
	}
	
	private void updateSequence(String nextSeq, GenomicRegion nextRegion){
		m_CurSeq += nextSeq;
		m_CurSeqRegion = m_CurSeqRegion.union(nextRegion, m_Comparator);
		if(!m_CurSeqRegion.getEnd().getChromosome().equals(m_CurSeqRegion.getStart().getChromosome())){
			Chromosome chr = m_CurSeqRegion.getEnd().getChromosome();
			if(m_ChrIndexes.containsKey(chr)){
				return;
			}
			int index = m_CurSeq.length() - (int)(m_CurSeqRegion.getEnd().getBaseIndex() - 1);
			m_ChrIndexes.put(chr, index);
		}
	}
	
	private GenomeWorker activateLocationQueries(){
		while(!m_LocationQueries.isEmpty() && this.regionContains(m_CurSeqRegion, m_LocationQueries.peek().getStart())){
			LocationQuery cur = m_LocationQueries.poll();
			m_ActiveLocationQueries.add(cur);
		}
		return this;
	}
	
	private GenomeWorker activateBoundedQueries(){
		while(!m_BoundedQueries.isEmpty() && this.regionContains(m_CurSeqRegion, m_BoundedQueries.peek().getStart())){
			LocationBoundedSequenceQuery cur = m_BoundedQueries.poll();
			m_ActiveBoundedQueries.add(cur);
		}
		return this;
	}
	
	private GenomicCoordinate getGenomicLocation(int index){
		Chromosome chr = null;
		int greatestLessThanIndex = -1;
		for(Chromosome c : m_ChrIndexes.keySet()){
			if(m_ChrIndexes.get(c) <= index && m_ChrIndexes.get(c) > greatestLessThanIndex){
				greatestLessThanIndex = m_ChrIndexes.get(c);
				chr = c;
			}
		}
		long base = index - greatestLessThanIndex + 1;
		return new GenomicCoordinate(chr, base);
	}
	
	private List<GenomicRegion> find(String target, GenomicCoordinate startLocation){
		List<GenomicRegion> results = new ArrayList<GenomicRegion>();
		//TODO
		return results;
	}
	
	private GenomeWorker processSequenceQueries(){
		for(SequenceQuery query : m_SequenceQueries){
			String target = query.getTargetSequence();
			List<GenomicRegion> results = this.find(target, m_LastLocationSearched.get(query));
			
		}
		return this;
	}
	
	@Override
	public void run() {
		while(!m_Provider.done()){
			if(!this.jobsRemaining()){
				m_Provider.processing();
				m_Provider.workerTerminated();
				break;
			}
			GenomicRegion nextRegion = m_Provider.getRegion();
			if(this.recordSequence(nextRegion)){
				this.updateSequence(m_Provider.getSequence(), nextRegion);
				m_Provider.processing();
				//do important work
				this.activateLocationQueries().activateBoundedQueries();
				
				
				m_Provider.processingComplete();
			}
		}
	}

}
