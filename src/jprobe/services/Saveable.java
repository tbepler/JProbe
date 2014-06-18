package jprobe.services;

import java.io.InputStream;
import java.io.OutputStream;

public interface Saveable {
	
	public boolean changedSinceSave();
	
	/**
	 * This method is called to save this saveable to the given OutputStream. The return value should be
	 * the number of bytes written to the stream. If the number of bytes written is incorrect, then load
	 * behavior cannot be guaranteed.
	 * @param out - OutputStream to write to
	 * @return - number of bytes written to the stream
	 */
	public long save(OutputStream out);
	
	
	public void load(InputStream in);
	
}
