package plugins.genome.util.reader.query;

import plugins.genome.util.GenomicSequence;

public interface QueryProcessor {
	
	public void process(GenomicSequence next);
	public boolean done();
	
}
