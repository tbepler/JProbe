package plugins.genome.services.reader;

import plugins.genome.services.utils.Genome;
import jprobe.services.function.ProgressListener;

public interface GenomeReader {
	
	public Genome getGenome();
	public void read(LocationQuery[] locationQueries, SequenceQuery[] sequenceQueries, LocationBoundedSequenceQuery[] boundedQueries);
	
	public void addProgressListener(ProgressListener listener);
	public void removeProgressListener(ProgressListener listener);
	
}
