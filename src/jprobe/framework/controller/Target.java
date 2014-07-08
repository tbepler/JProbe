package jprobe.framework.controller;

import java.io.OutputStream;

/**
 * This interface represents the target of an output operation. The target is specified
 * by given name and {@link OutputStream} object.
 * 
 * @author Tristan Bepler
 *
 */
public interface Target {
	
	/**
	 * Returns an identifier for this {@link Target}.
	 * @return
	 */
	public String getName();
	
	/**
	 * Returns the {@link OutputStream} object to which data should be written.
	 * @return
	 */
	public OutputStream getOutputStream();
	
}
