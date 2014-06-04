package jprobe.services;

public interface SaveListener {
	
	/**
	 * This method should be thread safe.
	 * @param e
	 */
	public void update(SaveEvent e);
	
}
