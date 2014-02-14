package plugins.genome;

import plugins.genome.services.utils.GenomicSequence;

public interface QueryProcessor {
	
	public void process(GenomicSequence next);
	public boolean allQueriesProcessed();
	
}
