package util.genome.reader.query;

import java.util.*;

import util.genome.GenomicCoordinate;
import util.genome.GenomicRegion;
import util.genome.GenomicSequence;

public class BoundedQueryProcessor implements QueryProcessor{
	
	private final Queue<LocationBoundedSequenceQuery> m_Remaining;
	private final Queue<LocationBoundedSequenceQuery> m_Active;
	private final Map<LocationBoundedSequenceQuery, GenomicCoordinate> m_ProcessedTo; 
	private GenomicSequence m_Seq;
	
	public BoundedQueryProcessor(List<LocationBoundedSequenceQuery> queries){
		m_Remaining = new PriorityQueue<LocationBoundedSequenceQuery>(queries.size(), LocationBoundedSequenceQuery.START_COMPARATOR);
		for(LocationBoundedSequenceQuery q : queries){
			m_Remaining.add(q);
		}
		m_Active = new PriorityQueue<LocationBoundedSequenceQuery>(10, LocationBoundedSequenceQuery.END_COMPARATOR);
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
		//get starting search location
		GenomicCoordinate startFrom;
		if(m_ProcessedTo.containsKey(query)){
			startFrom = m_ProcessedTo.get(query).increment(1);
		}else if(m_Seq.contains(query.getStart())){
			startFrom = query.getStart();
		}else{
			startFrom = m_Seq.getStart();
		}
		//get ending search location
		GenomicCoordinate searchTo;
		if(m_Seq.contains(query.getEnd())){
			searchTo = query.getEnd();
		}else{
			searchTo = m_Seq.getEnd();
		}
		//if the query region isn't large enough to fit the target sequence, abort
		if(startFrom.distance(searchTo) < query.getTargetSequence().length()){
			return;
		}
		//search through sequences starting at start location going to end location
		String target = query.getTargetSequence();
		GenomicSequence search = new GenomicSequence(target, new GenomicRegion(startFrom, startFrom.increment(target.length()-1)));
		while(search.getEnd().compareTo(searchTo) <= 0){
			if(m_Seq.contains(search)){
				query.process(search.getRegion());
			}
			search = new GenomicSequence(target, search.getRegion().increment(1));
		}
		//update processed to location to last search location's start
		m_ProcessedTo.put(query, search.getStart());
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
		for(LocationBoundedSequenceQuery query : m_Active){
			this.process(query);
		}
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
