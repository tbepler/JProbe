package datatypes;

import exceptions.IllegalStringException;

/**
 * This class extends Sequence and is used to represent peak objects. It contains the DNA sequence, chromosome, start and ending locations for the peak.
 * 
 * @author Tristan Bepler
 * @see DNASequence
 */

public class Peak extends DNASequence{
	
	private String chr;
	private int start;
	private int end;
	
	public Peak(String seq, String chr, int start, int end) throws IllegalStringException {
		super(seq);
		this.chr = chr;
		this.start = start;
		this.end = end;
	}
	
	public String getChr(){
		return chr;	
	}
	
	public int getStart(){
		return start;
	}
	
	public int getEnd(){
		return end;
	}

}
