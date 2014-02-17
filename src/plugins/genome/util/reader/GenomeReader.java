package plugins.genome.util.reader;

import plugins.genome.util.Genome;
import plugins.genome.util.reader.query.LocationBoundedSequenceQuery;
import plugins.genome.util.reader.query.LocationQuery;
import plugins.genome.util.reader.query.SequenceQuery;
import jprobe.services.function.ProgressListener;

public interface GenomeReader {
	
	public Genome getGenome();
	public void read(LocationQuery[] locationQueries, SequenceQuery[] sequenceQueries, LocationBoundedSequenceQuery[] boundedQueries);
	
	public void addProgressListener(ProgressListener listener);
	public void removeProgressListener(ProgressListener listener);
	
}
