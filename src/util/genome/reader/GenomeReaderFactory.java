package util.genome.reader;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import util.progress.ProgressListener;

public class GenomeReaderFactory {
	
	public static GenomeReader createGenomeReader(File genomeFile){
		return new BasicGenomeReader(genomeFile);
	}
	
	public static GenomeReader createGenomeReader(File genomeFile, Collection<ProgressListener> listeners){
		return new BasicGenomeReader(genomeFile, listeners);
	}
	
	public static GenomeReader createGenomeReader(File genomeFile, ProgressListener listener){
		Collection<ProgressListener> l = new ArrayList<ProgressListener>();
		l.add(listener);
		return new BasicGenomeReader(genomeFile, l);
	}
	
}
