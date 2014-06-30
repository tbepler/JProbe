package jprobe.services.data;

import java.io.InputStream;
import java.util.List;

import javax.swing.filechooser.FileFilter;

/**
 * This interface represents a class used to read Data objects. Classes implementing this interface
 * define methods for providing readable formats, providing the class of Data read, and reading data
 * from an input stream, using an appropriate format. This class is not intended for deserializing
 * Data objects, but rather for reading Data objects from user readable formats.
 * 
 * @author Tristan Bepler
 *
 * @param <D> - the Data class read by this object
 */
public interface DataReader<D extends Data> {
	
	/**
	 * Returns a list of read formats that can be used to read Data objects with this
	 * DataReader.
	 * @return
	 */
	public List<FileFilter> getReadFormats();
	
	/**
	 * Returns the Data class read by this DataReader.
	 * @return
	 */
	public Class<D> getReadClass();
	
	/**
	 * Read Data from the InputStream with the specified format.
	 * @param format - format that the data is in
	 * @param in - InputStream from which to read the data
	 * @return Data object read from the stream
	 * @throws ReadException - thrown if an error occurs while reading the stream
	 */
	public D read(FileFilter format, InputStream in) throws ReadException;
	
}
