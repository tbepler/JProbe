package old.datatypes.bindingsite;

import old.datatypes.sequence.Sequence;
import util.MathUtils;

/**
 * This class is used to represent binding sites associated with particular sequences and provides various methods
 * that may be useful in processing information relevant to binding sites.
 * @author Tristan Bepler
 *
 */
public class BindingSite {
	
	private Sequence seq;
	private int start;
	private int end;
	
	/**
	 * Create a new BindingSite instance. A binding site is defined by starting and ending locations, inclusive,
	 * and the sequence that contains this binding site. The start and end locations should be on the sequence,
	 * with start being less than end. Specifically they should adhere to (0<=start<=end{@literal <}seq.length()). If the
	 * start index is greater than the end index, then start and end values are flipped during construction. If either
	 * the start or end fall outside of the range of the sequence, they are clamped to that range
	 * (see {@link util.MathUtils#clamp}).
	 * @param seq the reference sequence for this binding site
	 * @param start the starting index, inclusive
	 * @param end the ending index, inclusive
	 */
	
	public BindingSite(Sequence seq, int start, int end){
		this.seq = seq;
		if(end < start){
			this.start = MathUtils.clamp(end, 0, seq.length()-1);
			this.end = MathUtils.clamp(start, 0, seq.length()-1);
		}else{
			this.start = MathUtils.clamp(start, 0, seq.length()-1);
			this.end = MathUtils.clamp(end, 0, seq.length()-1);
		}
	}
	
	/**
	 * Returns the index of the first position included in this binding site.
	 * @return starting index
	 */
	public int getStart(){
		return start;
	}
	
	/**
	 * Returns the index of the last position included in this binding site.
	 * @return ending index
	 */
	public int getEnd(){
		return end;
	}

	/**
	 * Returns the size of this binding site. In other words, this returns the value of (end index - start index + 1).
	 * @return the size of this binding site.
	 */
	public int getSize(){
		return end-start+1;
	}
	
	/**
	 * Returns an index marking the center of the binding site. This is computed by averaging the start and end indexes.
	 * If the size of the binding site is even, then the center returned is rounded down (eg. if the size is 4, then the
	 * value returned would be start+1).
	 * @return
	 */
	public int getCenter(){
		return (start+end)/2;
	}
	
	/**
	 * Returns the sequence covered by this site.
	 * @return the sequence covered by this site.
	 */
	public String getSiteSequence(){
		return seq.getSeq().substring(start,end+1);
	}
	
	/**
	 * Checks if the specified index is within this binding site. In other words, this returns true if
	 * (start<=index<=end) and false otherwise.
	 * @param index to be checked
	 * @return true if (start<=index<=end) and false otherwise
	 */
	public boolean contains(int index){
		return (start <= index && index <= end);
	}
}
