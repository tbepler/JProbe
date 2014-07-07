package util.genome.reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.List;

import util.genome.Chromosome;
import util.genome.GenomicCoordinate;
import util.genome.GenomicRegion;
import util.genome.GenomicSequence;
import util.genome.reader.query.BoundedQueryProcessor;
import util.genome.reader.query.LocationBoundedSequenceQuery;
import util.genome.reader.query.LocationQuery;
import util.genome.reader.query.LocationQueryProcessor;
import util.genome.reader.query.QueryProcessor;
import util.genome.reader.query.SequenceQuery;
import util.genome.reader.query.SequenceQueryProcessor;
import util.progress.ProgressEvent;
import util.progress.ProgressListener;

public class BasicGenomeReader extends AbstractGenomeReader{
	
	public static final int LINES_PER_NOTIFY = 10;
	
	private final File m_GenomeFile;
	
	public BasicGenomeReader(File genomeFile){
		m_GenomeFile = genomeFile;
	}
	
	public BasicGenomeReader(File genomeFile, Collection<ProgressListener> listeners){
		super(listeners);
		m_GenomeFile = genomeFile;
	}
	
	protected int notifyReadProgress(int progress, int maxProgress, int prevPercent){
		int percent = 100*progress/maxProgress;
		if(percent != prevPercent){
			this.notifyListeners(ProgressEvent.newProgressUpdate(this, progress, maxProgress));
		}
		return percent;
	}
	
	protected void notifyNewChromosome(Chromosome chr){
		this.notifyListeners(ProgressEvent.newMessageUpdate(this, "Reading "+m_GenomeFile.getName()+": "+chr));
	}
	
	protected void notifyCompleted(){
		this.notifyListeners(ProgressEvent.newCompletedEvent(this, "Done reading "+m_GenomeFile.getName()));
	}
	
	@Override
	public void read(List<LocationQuery> locationQueries, List<SequenceQuery> sequenceQueries, List<LocationBoundedSequenceQuery> boundedQueries) {
		LocationQueryProcessor locationProcessor = new LocationQueryProcessor(locationQueries);
		SequenceQueryProcessor sequenceProcessor = new SequenceQueryProcessor(sequenceQueries);
		BoundedQueryProcessor boundedProcessor = new BoundedQueryProcessor(boundedQueries);
		
		int totalQueries = locationQueries.size() + sequenceQueries.size() + boundedQueries.size();
		int queriesProcessed = 0;
		int percentProcessed = -1;
		
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(m_GenomeFile)));
			GenomicCoordinate seqStart = null;
			String line;
			try {
				while((line = reader.readLine()) != null && !done(locationProcessor, sequenceProcessor, boundedProcessor)){
					if(line.startsWith(">")){
						//new chromosome reached
						Chromosome chrom = Chromosome.getInstance(line);
						seqStart = new GenomicCoordinate(chrom, 1);
						this.notifyNewChromosome(chrom);
					}else{
						GenomicSequence seq = new GenomicSequence(line, new GenomicRegion(seqStart, seqStart.increment(line.length()-1)));
						queriesProcessed += locationProcessor.process(seq);
						queriesProcessed += sequenceProcessor.process(seq);
						queriesProcessed += boundedProcessor.process(seq);
						percentProcessed = notifyReadProgress(queriesProcessed, totalQueries, percentProcessed);
						seqStart = seq.getEnd().increment(1);
					}
				}
				reader.close();
			} catch (IOException e) {
				//do nothing
			}
			this.notifyCompleted();
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
		
	}

	private static boolean done(QueryProcessor ... processors){
		for(QueryProcessor p : processors){
			if(!p.done()) return false;
		}
		return true;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	

}
