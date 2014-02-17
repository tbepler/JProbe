package plugins.genome.util.reader.query;

import java.util.*;

import plugins.genome.util.GenomicCoordinate;
import plugins.genome.util.GenomicRegion;
import plugins.genome.util.GenomicSequence;

public class SequenceQueryProcessor implements QueryProcessor{
	
	private final List<SequenceQuery> m_Queries;
	private final Map<SequenceQuery, GenomicCoordinate> m_ProcessedTo;
	private final int m_LongestQuery;
	private GenomicSequence m_Seq;
	
	public SequenceQueryProcessor(List<SequenceQuery> queries){
		m_Queries = new ArrayList<SequenceQuery>(queries);
		m_ProcessedTo = new HashMap<SequenceQuery, GenomicCoordinate>();
		m_LongestQuery = this.findLongestQuery(m_Queries);
		m_Seq = null;
	}
	
	private int findLongestQuery(List<SequenceQuery> queries){
		int longest = 0;
		for(SequenceQuery q : queries){
			if(q.getTargetSequence().length() > longest){
				longest = q.getTargetSequence().length();
			}
		}
		return longest;
	}
	
	private void process(SequenceQuery query){
		//get starting search location
		GenomicCoordinate startFrom;
		if(m_ProcessedTo.containsKey(query)){
			startFrom = m_ProcessedTo.get(query).increment(1);
		}else{
			startFrom = m_Seq.getStart();
		}
		//get ending search location
		GenomicCoordinate searchTo = m_Seq.getEnd();
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
		//update sequence with next sequence
		if(m_Seq != null){
			m_Seq.join(next);
		}else{
			m_Seq = next;
		}
		//process
		for(SequenceQuery query : m_Queries){
			this.process(query);
		}
		//trim sequence
		if(m_LongestQuery <= 1){
			m_Seq = null;
		}else if(m_LongestQuery <= m_Seq.length()){
			m_Seq = m_Seq.subsequence(m_Seq.getEnd().decrement(m_LongestQuery-2));
		}
	}

	@Override
	public boolean done() {
		//never done till whole genome read
		return false;
	}

}
