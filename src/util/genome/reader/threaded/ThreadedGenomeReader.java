package util.genome.reader.threaded;

import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import util.genome.reader.AbstractGenomeReader;
import util.genome.reader.query.LocationBoundedSequenceQuery;
import util.genome.reader.query.LocationQuery;
import util.genome.reader.query.SequenceQuery;

public class ThreadedGenomeReader extends AbstractGenomeReader{
	
	private final int PROCESSORS = Runtime.getRuntime().availableProcessors();
	
	private final ExecutorService m_ThreadPool;
	
	public ThreadedGenomeReader(File genomeFile){
		m_ThreadPool = Executors.newFixedThreadPool(PROCESSORS > 1 ? PROCESSORS -1 : 1);
	}

	@Override
	public void read(List<LocationQuery> locationQueries, List<SequenceQuery> sequenceQueries, List<LocationBoundedSequenceQuery> boundedQueries) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setUpdateMode(UpdateMode mode) {
		// TODO Auto-generated method stub
		
	}


}
