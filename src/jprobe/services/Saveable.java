package jprobe.services;

import java.io.InputStream;
import java.io.OutputStream;

public interface Saveable {
	
	public boolean changedSinceSave();
	public void save(OutputStream out);
	public void load(InputStream in);
	
}
