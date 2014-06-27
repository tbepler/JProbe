package jprobe.services;

import java.util.Collection;
import java.util.List;

import jprobe.services.function.Function;

/**
 * JProbeCore is the core class of this software. It is responsible for maintaining the {@link Workspace}s and provides access
 * to registered {@link Function}s.
 * 
 * @author Tristan Bepler
 *
 */
public interface JProbeCore {
	
	/**
	 * This enum represents the mode's in which the core can operate.
	 * <P>
	 * {@link COMMAND} - The core is in command mode. It will parse command line arguments, execute the specified function, and then terminate.
	 * <P>
	 * {@link GUI} - The core is in GUI mode. It will not execute any command line arguments and will wait for a shutdown() call before terminating.
	 * 
	 * @author Tristan Bepler
	 *
	 */
	public enum Mode{
		/**
		 * The core is in command mode. It will parse command line arguments, execute the specified function, and then terminate.
		 */
		COMMAND,
		
		/**
		 * The core is in GUI mode. It will not execute any command line arguments and will wait for a shutdown() call before terminating.
		 */
		GUI;
		
		@Override
		public String toString(){
			switch(this){
			case COMMAND:
				return "command";
			case GUI:
				return "gui";
			default:
				return null;
			}
		}
		
	}
	
	/**
	 * Returns the {@link Mode} in which the core is currently operating.
	 * @return
	 */
	public Mode getMode();
	
	/**
	 * Returns the name of this software.
	 * @return
	 */
	public String getName();
	
	/**
	 * Returns the current version of this software.
	 * @return
	 */
	public String getVersion();
	
	/**
	 * Returns a string containing the path to the user's preferences directory. This is where all preferences or settings 
	 * files should be written.
	 * @return
	 */
	public String getPreferencesDir();
	
	/**
	 * Returns a string containing the path to the user's logs directory. This is where all additional log files
	 * should be written.
	 * @return
	 */
	public String getLogsDir();
	
	/**
	 * Returns a string containing the path to the user's specific directory. This is where any temporary or plugin state
	 * save files should be written.
	 * @return
	 */
	public String getUserDir();
	
	/**
	 * Adds a listener to this core in order to receive CoreEvents.
	 * @param listener
	 */
	public void addCoreListener(CoreListener listener);
	
	/**
	 * Removes the specified listener from this core.
	 * @param listener
	 */
	public void removeCoreListener(CoreListener listener);
	
	/**
	 * Shutdown the core. This will close the OSGI framework, calling stop() on all plugin bundles, and then 
	 * call System.exit()
	 */
	public void shutdown();
	
	/**
	 * Creates a new {@link Workspace}, adds it to the Workspace list, and returns it.
	 * @return
	 */
	public Workspace newWorkspace();
	
	/**
	 * Returns the {@link Workspace} at the given index, if it exists. If index < 0 or index >= {@link #numWorkspaces()},
	 * then this will return null.
	 * @param index - index of the Workspace to be returned
	 * @return - the requested Workspace or null if the index is out of bounds
	 */
	public Workspace getWorkspace(int index);
	
	/**
	 * Returns an unmodifiable list containing all the current {@link Workspace}s.
	 * @return
	 */
	public List<Workspace> getWorkspaces();
	
	/**
	 * Closes the specified {@link Workspace}, assuming it exists.
	 * @param index
	 */
	public void closeWorkspace(int index);
	
	/**
	 * Returns the number of currently opened {@link Workspace}s.
	 * @return
	 */
	public int numWorkspaces();
	
	/**
	 * Returns an unmodifiable collection containing the currently registered {@link Function}s. Functions can be
	 * registered by submitting a Function service through BundleContext received in a plugin's BundleActivator.start()
	 * method.
	 * @return unmodifiable collection of the registered Functions
	 */
	public Collection<Function<?>> getFunctions();
	
}
