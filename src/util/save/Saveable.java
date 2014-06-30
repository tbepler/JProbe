package util.save;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * This interface defines several methods for saving, loading, and importing data.
 * 
 * @author Tristan Bepler
 *
 */
public interface Saveable {
	
	/**
	 * Should return true if this object has unsaved changes. False otherwise.
	 * @return
	 */
	public boolean unsavedChanges();
	
	/**
	 * This method is called to save this saveable to the given OutputStream. The return value should be
	 * the number of bytes written to the stream. If the number of bytes written is incorrect, then load
	 * behavior cannot be guaranteed.
	 * @param out - OutputStream to write to
	 * @return - number of bytes written to the stream
	 */
	public long saveTo(OutputStream out, String outName) throws SaveException;
	
	/**
	 * Load this saveable objects data from the given InputStream. 
	 * @param in
	 */
	public void loadFrom(InputStream in, String sourceName) throws LoadException;
	
	/**
	 * Import data from the given InputStream into this saveable object.
	 * @param in
	 */
	public void importFrom(InputStream in, String sourceName) throws ImportException;
	
	/**
	 * Registers the given listener on this Saveable to receive events.
	 * @param l
	 */
	public void addSaveableListener(SaveableListener l);
	
	/**
	 * Removes the given listener from this Saveable.
	 * @param l
	 */
	public void removeSaveableListener(SaveableListener l);
	
}
