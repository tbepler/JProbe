package readwrite;

import java.io.BufferedWriter;
import java.util.Map;

import core.exceptions.FormatNotSupportedException;
import datatypes.DataType;

public interface DataWriter {
	
	public Map<String, String[]> getValidWriteFormats();
	public void write(DataType data, String format, BufferedWriter out) throws FormatNotSupportedException;

}
