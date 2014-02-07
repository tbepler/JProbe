package plugins.genome.services.reader;

import jprobe.services.function.ProgressListener;

public interface GenomeReader {
	
	public void read(LocationQuery[] locationQueries, SequenceQuery[] sequenceQueries, LocationBoundedSequenceQuery[] boundedQueries);
	
	public void addProgressListener(ProgressListener listener);
	public void removeProgressListener(ProgressListener listener);
	
}
