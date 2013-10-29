package datatypes.sequence;

import core.Constants;
import exceptions.IllegalStringException;

/**
 * This class is used to represent DNA sequences. It enforces that the string it contains is made
 * up entirely of characters allowed in a DNA sequence.
 * 
 * @author Tristan Bepler
 *
 */

public class DNASequence implements Sequence{
	
	private String seq;
	
	/**
	 * Returns a new Sequence object containing the specified sequence string. If the string contains non-DNA characters and IllegalStringException is thrown.
	 * @param seq string containing DNA characters
	 * @throws IllegalStringException
	 */
	public DNASequence(String seq) throws IllegalStringException{
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
	private void setSeq(String seq) throws IllegalStringException{
		if(seq.matches(Constants.DNA_REGEX)){
			this.seq = seq.toUpperCase();
		}else{
			throw new IllegalStringException("Error: the string \""+seq+"\" contains non-DNA characters.");
		}
	}

	@Override
	public String seqToString() {
		return seq;
	}

	@Override
	public int length() {
		return seq.length();
	}
	
	
	
}
