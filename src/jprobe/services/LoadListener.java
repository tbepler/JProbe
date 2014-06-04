package jprobe.services;

public interface LoadListener {
	
	/**
	 * This method should be thread safe.
	 * @param e
	 */
	public void update(LoadEvent e);
	
}
