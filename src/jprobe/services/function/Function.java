package jprobe.services.function;

import jprobe.services.data.Data;



public interface Function {
	
	/**
	 * Returns the name of this function.
	 * @return name of this function
	 */
	public String getName();
	
	/**
	 * Returns a description of what this function does.
	 * @return despcription of this function
	 */
	public String getDescription();
	
	public boolean isProgressTrackable();
	public int getProgressLength();
	public void addListener(ProgressListener listener);
	public void removeListener(ProgressListener listener);
	
	/**
	 * This method defines the core behavior of this function. This should never return null. It should throw an exception
	 * instead.
	 * @return a Data object that is the result of this function's processing
	 */
	public Data run() throws Exception;
}
