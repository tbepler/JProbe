package util.genome.reader.query;

import util.genome.GenomicSequence;

public interface QueryProcessor {
	
	public void process(GenomicSequence next);
	public boolean done();
	
}
