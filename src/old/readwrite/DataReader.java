package old.readwrite;

import java.util.Map;
import java.util.Scanner;

import old.core.exceptions.FileReadException;
import old.core.exceptions.FormatNotSupportedException;
import old.datatypes.DataType;

import org.w3c.dom.Element;

public interface DataReader {
	public Map<String, String[]> getValidReadFormats();
	public DataType read(String format, Scanner s) throws FormatNotSupportedException, FileReadException;
	public DataType readXML(Element e) throws FormatNotSupportedException, FileReadException;
}
