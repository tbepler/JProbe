package plugins.genome;

import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.TreeSet;

import plugins.genome.services.reader.LocationQuery;
import plugins.genome.services.utils.GenomicCoordinate;
import plugins.genome.services.utils.GenomicSequence;

public class LocationQueryProcessor implements QueryProcessor{
		
	private Queue<LocationQuery> m_Remaining;
	private Queue<LocationQuery> m_Active;
	private TreeSet<LocationQuery> m_ActiveStarts;
	private GenomicSequence m_Seq;
	
	public LocationQueryProcessor(List<LocationQuery> queries){
		m_Remaining = new PriorityQueue<LocationQuery>(queries.size(), LocationQuery.START_COMPARATOR);
		for(LocationQuery q : queries){
			m_Remaining.add(q);
		}
		m_Active = new PriorityQueue<LocationQuery>(10, LocationQuery.END_COMPARATOR);
		m_ActiveStarts = new TreeSet<LocationQuery>(LocationQuery.START_COMPARATOR);
		m_Seq = null;
	}
	
	private GenomicCoordinate getFirstActiveStart(){
		return m_ActiveStarts.first().getStart();
	}
	
	@Override
	public void process(GenomicSequence next) {
		if(m_Seq == null){
			m_Seq = next;
		}else{
			m_Seq = m_Seq.join(next);
		}
		//move queries that start in the region to the active queries q
		while(!m_Remaining.isEmpty() && m_Seq.contains(m_Remaining.peek().getStart())){
			LocationQuery cur = m_Remaining.poll();
			m_Active.add(cur);
			m_ActiveStarts.add(cur);
		}
		//process active queries that end within the current region
		while(!m_Active.isEmpty() && m_Seq.contains(m_Active.peek().getEnd())){
			LocationQuery cur = m_Active.poll();
			m_ActiveStarts.remove(cur);
			GenomicSequence subseq = m_Seq.subsequence(cur.getStart(), cur.getEnd());
			cur.process(subseq.getSequence());
		}
		//trim sequence
		m_Seq = m_Seq.subsequence(this.getFirstActiveStart());
		
	}

	@Override
	public boolean allQueriesProcessed() {
		return m_Remaining.isEmpty() && m_Active.isEmpty();
	}

}
