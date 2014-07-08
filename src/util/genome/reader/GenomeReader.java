package util.genome.reader;

import java.util.List;
import util.genome.reader.query.LocationBoundedSequenceQuery;
import util.genome.reader.query.LocationQuery;
import util.genome.reader.query.SequenceQuery;
import util.progress.ProgressListener;

public interface GenomeReader {

	public void read(
			List<LocationQuery> locationQueries,
			List<SequenceQuery> sequenceQueries,
			List<LocationBoundedSequenceQuery> boundedQueries,
			ProgressListener l
			);
	
}
