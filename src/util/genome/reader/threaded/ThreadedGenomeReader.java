package util.genome.reader.threaded;

import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import util.genome.reader.GenomeReader;
import util.genome.reader.query.LocationBoundedSequenceQuery;
import util.genome.reader.query.LocationQuery;
import util.genome.reader.query.SequenceQuery;
import util.progress.ProgressListener;

public class ThreadedGenomeReader implements GenomeReader{
	
	private final int PROCESSORS = Runtime.getRuntime().availableProcessors();
	
	private final ExecutorService m_ThreadPool;
	
	public ThreadedGenomeReader(File genomeFile){
		m_ThreadPool = Executors.newFixedThreadPool(PROCESSORS > 1 ? PROCESSORS -1 : 1);
	}

	@Override
	public void read(List<LocationQuery> locationQueries, List<SequenceQuery> sequenceQueries, List<LocationBoundedSequenceQuery> boundedQueries, ProgressListener l) {
		// TODO Auto-generated method stub
		
	}


}
