package jprobe.services.data;

import java.io.InputStream;
import javax.swing.filechooser.FileFilter;


public interface DataReader {
	public FileFilter[] getValidReadFormats();
	public Class<? extends Data> getReadClass();
	public Data read(FileFilter format, InputStream in) throws Exception;
}
