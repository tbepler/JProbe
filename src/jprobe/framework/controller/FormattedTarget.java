package jprobe.framework.controller;

/**
 * A target that also specifies a {@link Format} object in which output should be encoded.
 * 
 * @author Tristan Bepler
 *
 */
public interface FormattedTarget extends Target{
	
	/**
	 * Returns the {@link Format} in which output should be encoded.
	 * @return
	 */
	public Format getFormat();
	
}
