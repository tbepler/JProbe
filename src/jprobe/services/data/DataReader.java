package jprobe.services.data;

import java.io.InputStream;
import javax.swing.filechooser.FileFilter;


public interface DataReader {
	public FileFilter[] getValidReadFormats();
	public Data read(FileFilter format, InputStream in) throws Exception;
}
