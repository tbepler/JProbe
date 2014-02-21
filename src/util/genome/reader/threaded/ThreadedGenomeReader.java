package util.genome.reader.threaded;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import jprobe.services.ErrorHandler;
import plugins.genome.GenomeActivator;
import util.genome.Genome;
import util.genome.reader.AbstractGenomeReader;
import util.genome.reader.query.LocationBoundedSequenceQuery;
import util.genome.reader.query.LocationQuery;
import util.genome.reader.query.SequenceQuery;
import util.progress.ProgressEvent;

public class ThreadedGenomeReader extends AbstractGenomeReader{
	
	private final int PROCESSORS = Runtime.getRuntime().availableProcessors();
	
	private final ExecutorService m_ThreadPool;
	private final Genome m_Genome;
	
	public ThreadedGenomeReader(File genomeFile){
		m_Genome = this.prereadGenome(genomeFile);
		m_ThreadPool = Executors.newFixedThreadPool(PROCESSORS > 1 ? PROCESSORS -1 : 1);
	}
	
	@Override
	public Genome getGenome(){
		return m_Genome;
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
