package readwrite;

import java.io.BufferedWriter;
import java.util.Map;

import exceptions.FormatNotSupportedException;

public interface DataWriter {
	
	public Map<String, String[]> getValidWriteFormats();
	public void write(String format, BufferedWriter out) throws FormatNotSupportedException;

}
