package util.genome.reader.query;

import util.genome.GenomicSequence;

public interface QueryProcessor {
	
	/**
	 * Returns the number of queries processed
	 * @param next
	 * @return
	 */
	public int process(GenomicSequence next);
	
	/**
	 * Returns the original number of queries in this QueryProcessor.
	 * @return
	 */
	public int totalQueries();
	
	public boolean done();
	
}
