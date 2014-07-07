package jprobe.framework.controller;

import java.io.InputStream;

/**
 * This interface represents a data source as a named {@link InputStream} object.
 * 
 * @author Tristan Bepler
 *
 */
public interface Source {
	
	/**
	 * Returns an identifier for this source.
	 * @return
	 */
	public String getName();
	
	/**
	 * Returns the {@link InputStream} used to read data from this source.
	 * @return
	 */
	public InputStream getInputStream();
	
}
