package jprobe.services;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.List;

import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileFilter;

import util.save.LoadException;
import jprobe.services.data.Data;
import jprobe.services.data.DataReader;
import jprobe.services.data.DataWriter;
import jprobe.services.data.ReadException;
import jprobe.services.data.WriteException;
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
	 * Opens a new {@link Workspace} loaded from the given InputStream.
	 * @param in - InputStream from which to load the Workspace
	 * @param source - String naming the source of the InputStream
	 * @return - the opened Workspace
	 * @throws LoadException - thrown if an error occurs while loading the Workspace from the InputStream
	 */
	public Workspace openWorkspace(InputStream in, String source) throws LoadException;
	
	/**
	 * Returns the {@link Workspace} at the given index, if it exists. If index < 0 or index >= {@link #numWorkspaces()},
	 * then this will return null.
	 * @param index - index of the Workspace to be returned
	 * @return - the requested Workspace or null if the index is out of bounds
	 */
	public Workspace getWorkspace(int index);
	
	/**
	 * Returns the index of the given {@link Workspace} or -1 if the Workspace is not in the core.
	 * @param w
	 * @return
	 */
	public int indexOf(Workspace w);
	
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
	 * Closes the specified {@link Workspace}.
	 * @param w
	 */
	public void closeWorkspace(Workspace w);
	
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
	
	/**
	 * Returns a collection containing the currently readable Data classes. Data is read using the {@link DataReader} class. DataReaders can be
	 * registered by submitting a DataReader service through the BundleContext received in a plugin's BundleActivator.start()
	 * method.
	 * @return collection containing the readable Data classes
	 */
	public Collection<Class<? extends Data>> getReadableDataClasses();
	
	/**
	 * Returns a list of FileFilters representing the readable formats for the given Data class.
	 * @param readClass
	 * @return
	 */
	public List<FileFilter> getReadFormats(Class<? extends Data> readClass);
	
	/**
	 * Reads a Data object of the specified class from the given InputStream using the format passed.
	 * @param readClass - Data class that will be read
	 * @param format - format to read
	 * @param in - InputStream from which Data is read
	 * @return {@link Data} object read from the InputStream
	 * @throws ReadException thrown if an error occurs while reading the InputStream, if the Data class is not readable,
	 * or if the format is not valid for the Data class.
	 */
	public <D extends Data> D readData(Class<D> readClass, FileFilter format, InputStream in) throws ReadException;
	
	/**
	 * Returns true if there exists at least one {@link DataReader} for the given Data class. Otherwise, returns false.
	 * @param dataClass
	 * @return
	 */
	public boolean isReadable(Class<? extends Data> dataClass);
	
	/**
	 * Returns a collection containing the currently writable Data classes. Data is written using the {@link DataWriter} class. DataWriters can be
	 * registered by submitting a DataWriter service through the BundleContext received in a plugin's BundleActivator.start()
	 * method.
	 * @return unmodifiable collection of the registered DataWriters
	 */
	public Collection<Class<? extends Data>> getWritableDataClasses();
	
	/**
	 * Returns a list of FileNameExtensionFilters representing the writable formats for the given Data class.
	 * @param writeClass
	 * @return
	 */
	public List<FileNameExtensionFilter> getWriteFormats(Class<? extends Data> writeClass);
	
	/**
	 * Writes the given Data object to the specified OutputStream using the format passed.
	 * @param d - Data object to write
	 * @param format - format in which the Data object should be written
	 * @param out - OutputStreamthe Data object will be written to
	 * @throws WriteException - thrown if an error occurs while writing the Data object to the OutputStream, if the Data object is
	 * not writable, or if the format is not valid for the given Data object.
	 */
	public void writeData(Data d, FileNameExtensionFilter format, OutputStream out) throws WriteException;
	
	/**
	 * Returns true if there exists at least one (@link DataWriter} for the given Data class. Otherwise, returns false.
	 * @param dataClass
	 * @return
	 */
	public boolean isWritable(Class<? extends Data> dataClass);
	
}
