package plugins.genome.util.reader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import plugins.genome.util.Genome;
import plugins.genome.util.reader.query.LocationBoundedSequenceQuery;
import plugins.genome.util.reader.query.LocationQuery;
import plugins.genome.util.reader.query.SequenceQuery;
import jprobe.services.function.ProgressListener;

public interface GenomeReader {
	
	public static final Map<String, Genome> GENOME_HASH = new HashMap<String, Genome>();
	
	public Genome getGenome();
	public void read(List<LocationQuery> locationQueries, List<SequenceQuery> sequenceQueries, List<LocationBoundedSequenceQuery> boundedQueries);
	
	public void addProgressListener(ProgressListener listener);
	public void removeProgressListener(ProgressListener listener);
	
}
