package plugins.genome;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import jprobe.services.ErrorHandler;
import jprobe.services.function.ProgressEvent;
import jprobe.services.function.ProgressEvent.Type;
import plugins.genome.services.reader.AbstractGenomeReader;
import plugins.genome.services.reader.LocationBoundedSequenceQuery;
import plugins.genome.services.reader.LocationQuery;
import plugins.genome.services.reader.SequenceQuery;
import plugins.genome.services.utils.Chromosome;
import plugins.genome.services.utils.Genome;
import plugins.genome.services.utils.GenomicCoordinate;

public class ThreadedGenomeReader extends AbstractGenomeReader{
	
	private static final Map<String, Genome> GENOME_HASH = new HashMap<String, Genome>();
	private static final String CHR_TAG = ">chr";
	
	private final int PROCESSORS = Runtime.getRuntime().availableProcessors();
	
	private final ExecutorService m_ThreadPool;
	private final Genome m_Genome;
	
	public ThreadedGenomeReader(File genomeFile){
		m_Genome = this.getGenome(genomeFile);
		m_ThreadPool = Executors.newFixedThreadPool(PROCESSORS > 1 ? PROCESSORS -1 : 1);
	}
	
	protected Genome getGenome(File genomeFile){
		String key = genomeFile.getAbsolutePath();
		synchronized(GENOME_HASH){	
			if(GENOME_HASH.containsKey(key)){
				return GENOME_HASH.get(key);
			}
		}
		try {
			this.notifyListeners(new ProgressEvent(this, ProgressEvent.Type.UPDATE, 0, "Prereading genome file: "+genomeFile.getPath(), true));
			Genome genome = new Genome(genomeFile.getName(), new Scanner(genomeFile));
			synchronized(GENOME_HASH){
				GENOME_HASH.put(key, genome);
			}
			return genome;
		} catch (FileNotFoundException e) {
			ErrorHandler.getInstance().handleException(e, GenomeActivator.getBundle());
		}
		return null;
	}
	
	@Override
	public Genome getGenome(){
		return m_Genome;
	}

	@Override
	public void read(LocationQuery[] locationQueries, SequenceQuery[] sequenceQueries, LocationBoundedSequenceQuery[] boundedQueries) {
		//TODO
	}


}
