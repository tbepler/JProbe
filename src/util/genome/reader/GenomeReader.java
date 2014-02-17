package util.genome.reader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import util.genome.Genome;
import util.genome.reader.query.LocationBoundedSequenceQuery;
import util.genome.reader.query.LocationQuery;
import util.genome.reader.query.SequenceQuery;
import util.progress.ProgressListener;

public interface GenomeReader {
	
	public static final Map<String, Genome> GENOME_HASH = new HashMap<String, Genome>();
	
	public Genome getGenome();
	public void read(List<LocationQuery> locationQueries, List<SequenceQuery> sequenceQueries, List<LocationBoundedSequenceQuery> boundedQueries);
	
	public void addProgressListener(ProgressListener listener);
	public void removeProgressListener(ProgressListener listener);
	
}
