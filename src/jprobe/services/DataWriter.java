package jprobe.services;

import java.io.BufferedWriter;
import java.util.Map;

public interface DataWriter {
	
	public Map<String, String[]> getValidWriteFormats();
	public void write(Data data, String format, BufferedWriter out) throws Exception;

}
