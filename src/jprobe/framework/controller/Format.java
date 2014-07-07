package jprobe.framework.controller;

import java.io.File;

/**
 * Interface representing a data format.
 * 
 * @author Tristan Bepler
 *
 */
public interface Format {
	
	/**
	 * Returns true if the given {@link File} is in this {@link Format}, false otherwise.
	 * @param f - File to check if it is in this Format
	 * @return True if the File is in this Format, False otherwise
	 */
	public boolean accept(File f);
	
	/**
	 * Returns the name of this {@link Format}.
	 * @return a String containing this Format's name
	 */
	public String getName();
	
	/**
	 * Returns a description of this {@link Format}.
	 * @return a String containing this Format's description
	 */
	public String getDescription();
	
	/**
	 * Returns the file extensions associated with this {@link Format}.
	 * @return an Array of Strings representing file extensions associated with this Format
	 */
	public String[] getExtensions();
	
	
}
