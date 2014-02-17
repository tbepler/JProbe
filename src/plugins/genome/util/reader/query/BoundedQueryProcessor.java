package plugins.genome.util.reader.query;

import java.util.*;

import plugins.genome.util.GenomicCoordinate;
import plugins.genome.util.GenomicSequence;

public class BoundedQueryProcessor implements QueryProcessor{
	
	private Queue<LocationBoundedSequenceQuery> m_Remaining;
	private Queue<LocationBoundedSequenceQuery> m_Active;
	private TreeSet<LocationBoundedSequenceQuery> m_ActiveStarts;
	private Map<LocationBoundedSequenceQuery, GenomicCoordinate> m_ProcessedTo; 
	private GenomicSequence m_Seq;
	
	public BoundedQueryProcessor(List<LocationBoundedSequenceQuery> queries){
		m_Remaining = new PriorityQueue<LocationBoundedSequenceQuery>(queries.size(), LocationBoundedSequenceQuery.START_COMPARATOR);
		for(LocationBoundedSequenceQuery q : queries){
			m_Remaining.add(q);
		}
		m_Active = new PriorityQueue<LocationBoundedSequenceQuery>(10, LocationBoundedSequenceQuery.END_COMPARATOR);
		m_ActiveStarts = new TreeSet<LocationBoundedSequenceQuery>(LocationBoundedSequenceQuery.START_COMPARATOR);
		m_ProcessedTo = new HashMap<LocationBoundedSequenceQuery, GenomicCoordinate>();
		m_Seq = null;
	}
	
	private int longestQuery(){
		int longest = 0;
		for(LocationBoundedSequenceQuery q : m_Active){
			if(q.getTargetSequence().length() > longest){
				longest = q.getTargetSequence().length();
			}
		}
		return longest;
	}
	
	private void process(LocationBoundedSequenceQuery query){
		GenomicCoordinate startFrom;
		if(m_ProcessedTo.containsKey(query)){
			startFrom = m_ProcessedTo.get(query).increment(1);
		}else{
			startFrom = m_Seq.getStart();
		}
		GenomicCoordinate searchTo;
		if(m_Seq.contains(query.getEnd())){
			searchTo = query.getEnd();
		}else{
			searchTo = m_Seq.getEnd();
		}
	}
	
	@Override
	public void process(GenomicSequence next) {
		//update the current sequence with the new sequence
		if(m_Seq != null){
			m_Seq = m_Seq.join(next);
		}else{
			m_Seq = next;
		}
		//move remaining queries that start in sequence to active queries
		while(!m_Remaining.isEmpty() && m_Seq.contains(m_Remaining.peek().getStart())){
			LocationBoundedSequenceQuery query = m_Remaining.poll();
			m_Active.add(query);
		}
		//process active queries
		
		//remove active queries that end in the current sequence
		while(!m_Active.isEmpty() && m_Seq.contains(m_Active.peek().getEnd())){
			LocationBoundedSequenceQuery query = m_Active.poll();
			m_ProcessedTo.remove(query);
		}
		//trim sequence
		int longestQuery = this.longestQuery();
		if(longestQuery <= 1){
			m_Seq = null;
		}else if(longestQuery <= m_Seq.length()){
			m_Seq = m_Seq.subsequence(m_Seq.getEnd().decrement(longestQuery-2));
		}
	}

	@Override
	public boolean done() {
		return m_Remaining.isEmpty() && m_Active.isEmpty();
	}

}
