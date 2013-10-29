package core.datatypes;

import core.Constants;
import exception.IllegalStringException;

/**
 * This class is used to represent DNA sequences. It enforces that the string it contains is made
 * up entirely of characters allowed in a DNA sequence.
 * 
 * @author Tristan Bepler
 *
 */

public class Sequence {
	
	private String seq;
	
	/**
	 * Returns a new Sequence object containing the specified sequence.
	 * @param seq string containing DNA characters
	 * @throws IllegalStringException
	 */
	public Sequence(String seq) throws IllegalStringException{
		this.setSeq(seq);
	}
	
	/**
	 * Returns the sequence string contained by this object.
	 * @return the sequence contained by this object as a string
	 */
	public String getSeq(){
		return seq;
	}
	
	/**
	 * Sets this objects sequence to the specified string. If the string contains non-DNA characters an IllegalStringException is thrown.
	 * @param seq string containing DNA characters
	 * @throws IllegalStringException
	 */
	public void setSeq(String seq) throws IllegalStringException{
		if(seq.matches(Constants.DNA_REGEX)){
			this.seq = seq;
		}else{
			throw new IllegalStringException("Error: the string \""+seq+"\" contains a non DNA character.");
		}
	}
	
	
	
}
