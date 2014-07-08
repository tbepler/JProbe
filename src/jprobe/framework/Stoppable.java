package jprobe.framework;

/**
 * This interface defines methods for stopping an object and waiting for it to stop.
 * 
 * @author Tristan Bepler
 *
 */
public interface Stoppable {
	
	/**
	 * Signals this object that it should deactivate.
	 */
	public void stop();
	
	/**
	 * Blocks the calling thread until this object has stopped or the timeout
	 * duration has expired, whichever occurs first.
	 * @param timeout - millis to wait for this object to stop, a value of 0
	 * or less indicates to wait indefinitely
	 * @throws InterruptedException - thrown if the blocking thread is interrupted
	 * while waiting
	 */
	public void waitForStop(long timeout) throws InterruptedException;
	
}
