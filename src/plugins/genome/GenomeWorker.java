package plugins.genome;

import java.util.List;
import java.util.Queue;

import plugins.genome.services.reader.LocationBoundedSequenceQuery;
import plugins.genome.services.reader.LocationQuery;
import plugins.genome.services.reader.SequenceQuery;

public class GenomeWorker implements Runnable{
	
	private final SequenceProvider m_Provider;
	private final Queue<LocationQuery> m_LocationQueries;
	private final Queue<LocationBoundedSequenceQuery> m_BoundedQueries;
	private final List<SequenceQuery> m_SequenceQueries;
	
	public GenomeWorker(SequenceProvider provider, Queue<LocationQuery> locationQueries, Queue<LocationBoundedSequenceQuery> boundedQueries,
			List<SequenceQuery> sequenceQueries){
		m_Provider = provider;
		m_LocationQueries = locationQueries;
		m_BoundedQueries = boundedQueries;
		m_SequenceQueries = sequenceQueries;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

}
