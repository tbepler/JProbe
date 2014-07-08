package util.genome.reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
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
import util.progress.ProgressListener;

public class BasicGenomeReader implements GenomeReader{
	
	private final File m_GenomeFile;
	
	public BasicGenomeReader(File genomeFile){
		m_GenomeFile = genomeFile;
	}
	
	protected int notifyReadProgress(ProgressListener l, int progress, int maxProgress, int prevPercent, String message){
		int percent = 100*progress/maxProgress;
		if(percent != prevPercent){
			l.progressUpdate(percent, message);
		}
		return percent;
	}
	
	protected String notifyNewChromosome(ProgressListener l, Chromosome chr, int percent){
		String message = "Reading "+m_GenomeFile.getName()+": "+chr;
		l.progressUpdate(percent, message);
		return message;
	}
	
	protected void notifyCompleted(ProgressListener l){
		l.onCompletion("Done reading "+m_GenomeFile.getName());
	}
	
	protected void notifyStart(ProgressListener l){
		l.onStart(null);
	}
	
	@Override
	public void read(
			List<LocationQuery> locationQueries,
			List<SequenceQuery> sequenceQueries,
			List<LocationBoundedSequenceQuery> boundedQueries,
			ProgressListener l
			) {
		LocationQueryProcessor locationProcessor = new LocationQueryProcessor(locationQueries);
		SequenceQueryProcessor sequenceProcessor = new SequenceQueryProcessor(sequenceQueries);
		BoundedQueryProcessor boundedProcessor = new BoundedQueryProcessor(boundedQueries);
		
		if(l != null){
			read(l, locationProcessor, sequenceProcessor, boundedProcessor);
		}else{
			read(locationProcessor, sequenceProcessor, boundedProcessor);
		}
		
	}

	private void read(ProgressListener l, QueryProcessor ... processors) {
		int totalQueries = 0;
		for(QueryProcessor processor : processors){
			totalQueries += processor.totalQueries();
		}
		int queriesProcessed = 0;
		int percentProcessed = 0;
		String message = null;
		
		try {
			this.notifyStart(l);
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(m_GenomeFile)));
			GenomicCoordinate seqStart = null;
			String line;
			try {
				while((line = reader.readLine()) != null && !done(processors)){
					if(line.startsWith(">")){
						//new chromosome reached
						Chromosome chrom = Chromosome.getInstance(line);
						seqStart = new GenomicCoordinate(chrom, 1);
						message = this.notifyNewChromosome(l, chrom, percentProcessed);
					}else{
						GenomicSequence seq = new GenomicSequence(line, new GenomicRegion(seqStart, seqStart.increment(line.length()-1)));
						for(QueryProcessor processor : processors){
							queriesProcessed += processor.process(seq);
						}
						percentProcessed = this.notifyReadProgress(l, queriesProcessed, totalQueries, percentProcessed, message);
						seqStart = seq.getEnd().increment(1);
					}
				}
				reader.close();
			} catch (IOException e) {
				//do nothing
			}
			this.notifyCompleted(l);
		} catch (FileNotFoundException e) {
			l.onError(e);
			l.onCompletion(null);
			throw new RuntimeException(e);
		}
	}
	
	private void read(QueryProcessor ... processors){
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(m_GenomeFile)));
			GenomicCoordinate seqStart = null;
			String line;
			try {
				while((line = reader.readLine()) != null && !done(processors)){
					if(line.startsWith(">")){
						//new chromosome reached
						Chromosome chrom = Chromosome.getInstance(line);
						seqStart = new GenomicCoordinate(chrom, 1);
					}else{
						GenomicSequence seq = new GenomicSequence(line, new GenomicRegion(seqStart, seqStart.increment(line.length()-1)));
						for(QueryProcessor processor : processors){
							processor.process(seq);
						}
						seqStart = seq.getEnd().increment(1);
					}
				}
				reader.close();
			} catch (IOException e) {
				//do nothing
			}
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
