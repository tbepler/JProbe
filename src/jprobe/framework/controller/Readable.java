package jprobe.framework.controller;

import java.util.Collection;

/**
 * This interface defines a readable type. It specifies the class of object read
 * and the formats from which that class can be read.
 * 
 * @author Tristan Bepler
 *
 */
public interface Readable{
	
	/**
	 * Returns the class of object that is readable.
	 * @return
	 */
	public Class<?> readType();
	
	/**
	 * Returns a collection of {@link Format}s in which this class can be read.
	 * @return
	 */
	public Collection<Format> formats();
	
}
