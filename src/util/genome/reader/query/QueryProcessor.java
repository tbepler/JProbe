package util.genome.reader.query;

import util.genome.GenomicSequence;

public interface QueryProcessor {
	
	/**
	 * Returns the number of queries processed
	 * @param next
	 * @return
	 */
	public int process(GenomicSequence next);
	public boolean done();
	
}
