package jprobe.services.data;

import java.io.OutputStream;
import java.util.List;

import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * This interface represents a class used to write Data objects. Classes implementing this interface define
 * methods for providing writable formats, the writable Data class, and for writing Data objects of the 
 * specified class to an OutputStream using a specified format.
 * 
 * @author Tristan Bepler
 *
 * @param <D> - Data class written by this class.
 */
public interface DataWriter<D extends Data> {
	
	/**
	 * Returns a list of formats that this DataWriter can write.
	 * @return
	 */
	public List<FileNameExtensionFilter> getWriteFormats();
	
	/**
	 * Returns the Data class writen by this DataWriter.
	 * @return
	 */
	public Class<D> getWriteClass();
	
	/**
	 * Writes the given Data object to the provided OutputStream using the specified format. This is not intended
	 * for merely serializing the Data object, but, rather, for writing the object in a user readable manner.
	 * @param data - data object to write
	 * @param format - format in which the data should be written
	 * @param out - OutputStream to write the data to
	 * @throws WriteException - thrown if an error occurs while writing the data
	 */
	public void write(D data, FileNameExtensionFilter format, OutputStream out) throws WriteException;

}
