package util.genome.reader.query;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.TreeSet;

import util.genome.Chromosome;
import util.genome.GenomicCoordinate;
import util.genome.GenomicSequence;

public class LocationQueryProcessor implements QueryProcessor{
	
	private final Map<Chromosome, Queue<LocationQuery>> m_Remaining = new HashMap<Chromosome, Queue<LocationQuery>>();
	private final Queue<LocationQuery> m_Active;
	private final TreeSet<LocationQuery> m_ActiveStarts;
	private GenomicSequence m_Seq;
	private Chromosome m_CurChrom = null;
	private Queue<LocationQuery> m_CurQueries = null;
	
	public LocationQueryProcessor(List<LocationQuery> queries){
		for(LocationQuery q : queries){
			Chromosome chrom = q.getChromosome();
			if(m_Remaining.containsKey(chrom)){
				m_Remaining.get(chrom).add(q);
			}else{
				Queue<LocationQuery> queue = new PriorityQueue<LocationQuery>(10, LocationQuery.START_COMPARATOR);
				queue.add(q);
				m_Remaining.put(chrom, queue);
			}
		}
		m_Active = new PriorityQueue<LocationQuery>(10, LocationQuery.END_COMPARATOR);
		m_ActiveStarts = new TreeSet<LocationQuery>(LocationQuery.START_COMPARATOR);
		m_Seq = null;
		m_CurQueries = null;
	}
	
	private GenomicCoordinate getFirstActiveStart(){
		return m_ActiveStarts.first().getStart();
	}
	
	@Override
	public int process(GenomicSequence next) {
		int numProcessed = 0;
		if(done()){
			return numProcessed;
		}
		if(m_Seq == null){
			m_Seq = next;
		}else{
			m_Seq = m_Seq.join(next);
		}
		Chromosome chrom = m_Seq.getChromosome();
		//update the current chromosome and queries to the new chromosome if it has changed
		if(!chrom.equals(m_CurChrom)){
			m_Active.clear();
			m_ActiveStarts.clear();
			m_CurChrom = chrom;
			m_CurQueries = m_Remaining.get(m_CurChrom);
			m_Remaining.remove(m_CurChrom);
		}
		//move queries that start in the region to the active queries q
		if(m_CurQueries != null){
			while(!m_CurQueries.isEmpty() && m_Seq.contains(m_CurQueries.peek().getStart())){
				LocationQuery cur = m_CurQueries.poll();
				m_Active.add(cur);
				m_ActiveStarts.add(cur);
			}
		}
		//process active queries that end within the current region
		while(!m_Active.isEmpty() && m_Seq.contains(m_Active.peek().getEnd())){
			LocationQuery cur = m_Active.poll();
			m_ActiveStarts.remove(cur);
			GenomicSequence subseq = m_Seq.subsequence(cur.getStart(), cur.getEnd());
			cur.process(subseq);
			++numProcessed;
		}
		//trim sequence
		if(!m_Active.isEmpty()){
			GenomicCoordinate firstStart = this.getFirstActiveStart();
			if(m_Seq.contains(firstStart)){
				m_Seq = m_Seq.subsequence(firstStart);
			}else{
				m_Seq = null;
			}
		}else{
			m_Seq = null;
		}
		
		return numProcessed;
	}

	@Override
	public boolean done() {
		return m_Remaining.isEmpty() && m_Active.isEmpty() && (m_CurQueries == null || m_CurQueries.isEmpty());
	}

}
