package plugins.genome;

import java.io.File;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import plugins.genome.services.reader.AbstractGenomeReader;
import plugins.genome.services.reader.LocationBoundedSequenceQuery;
import plugins.genome.services.reader.LocationQuery;
import plugins.genome.services.reader.SequenceQuery;
import plugins.genome.services.utils.GenomicLocation;

public class ThreadedGenomeReader extends AbstractGenomeReader{
	
	private static final Map<String, Comparator<GenomicLocation>> GENOME_INDEX_HASH = new HashMap<String, Comparator<GenomicLocation>>();
	
	
	private final int PROCESSORS = Runtime.getRuntime().availableProcessors();
	
	private ExecutorService m_ThreadPool;
	
	public ThreadedGenomeReader(File genome){
		m_ThreadPool = Executors.newFixedThreadPool(PROCESSORS > 1 ? PROCESSORS -1 : 1);
	}

	protected Comparator<GenomicLocation> getLocationComparator(File genome){
		String key = genome.getAbsolutePath();
		if(GENOME_INDEX_HASH.containsKey(key)){
			return GENOME_INDEX_HASH.get(key);
		}else{
			
		}
	}
	
	private Comparator<GenomicLocation> prereadGenome(File genome){
		
	}

	@Override
	public void read(LocationQuery[] locationQueries, SequenceQuery[] sequenceQueries, LocationBoundedSequenceQuery[] boundedQueries) {
		// TODO Auto-generated method stub
		
	}

}
