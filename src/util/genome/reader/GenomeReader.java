package util.genome.reader;

import java.util.List;
import util.genome.reader.query.LocationBoundedSequenceQuery;
import util.genome.reader.query.LocationQuery;
import util.genome.reader.query.SequenceQuery;
import util.progress.ProgressListener;

public interface GenomeReader {
	
	public enum UpdateMode{
		FULL,
		NONE;
	}
	
	public void setUpdateMode(UpdateMode mode);
	public void read(List<LocationQuery> locationQueries, List<SequenceQuery> sequenceQueries, List<LocationBoundedSequenceQuery> boundedQueries);
	
	public void addProgressListener(ProgressListener listener);
	public void removeProgressListener(ProgressListener listener);
	
}
