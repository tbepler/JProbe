package readwrite;

import java.io.BufferedWriter;

import exceptions.FormatNotSupportedException;

public interface DataWriter {
	
	public String[] getValidWriteFormats();
	public void write(String format, BufferedWriter out) throws FormatNotSupportedException;

}
