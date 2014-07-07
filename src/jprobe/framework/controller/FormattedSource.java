package jprobe.framework.controller;

/**
 * A source that also specifies the {@link Format} in which it is encoded.
 * 
 * @author Tristan Bepler
 *
 */
public interface FormattedSource extends Source{
	
	/**
	 * Returns the {@link Format} of this source.
	 * @return
	 */
	public Format getFormat();
	
}
