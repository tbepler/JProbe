package old.readwrite;

import java.io.BufferedWriter;
import java.util.Map;

import old.core.exceptions.FormatNotSupportedException;
import old.datatypes.DataType;

public interface DataWriter {
	
	public Map<String, String[]> getValidWriteFormats();
	public void write(DataType data, String format, BufferedWriter out) throws FormatNotSupportedException;

}
