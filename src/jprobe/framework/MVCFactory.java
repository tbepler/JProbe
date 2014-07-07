package jprobe.framework;

import java.util.Properties;

import jprobe.framework.controller.Controller;
import jprobe.framework.model.Model;
import jprobe.framework.view.PersistentView;

/**
 * This interface defines a factory that can be used to create {@link Model}, {@link PersistentView}, and {@link Controller} objects.
 * 
 * @author Tristan Bepler
 *
 */
public interface MVCFactory {
	
	/**
	 * Starts this factory, reading properties from the given {@link Properties} object.
	 * @param props
	 */
	public void start(Properties props);
	
	/**
	 * Stops this factory, storing properties in the given {@link Properties} object.
	 * @param props
	 */
	public void stop(Properties props);
	
	/**
	 * Blocks the calling {@link Thread} until this {@link MVCFactory} has stopped or the timeout
	 * duration has expired, whichever occurs first.
	 * @param timeout - maximum duration to wait for this factory to stop, values less than 1 indicate
	 * to wait indefinitely
	 * @throws InterruptedException - thrown if the blocking Thread is interrupted
	 */
	public void waitForStop(long timeout) throws InterruptedException;
	
	/**
	 * Returns a new {@link Model} constructed according to this {@link MVCFactory}'s implementation.
	 * @return
	 */
	public Model newModel();
	
	/**
	 * Returns a new {@link Controller} constructed according to this {@link MVCFactory}'s implementation.
	 * @return
	 */
	public Controller newController();
	
	/**
	 * Returns a new {@link PersistentView} constructed according to this {@link MVCFactory}'s implementation.
	 * @return
	 */
	public PersistentView newView();
	

}
