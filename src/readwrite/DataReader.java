package readwrite;

import java.util.Map;
import java.util.Scanner;

import org.w3c.dom.Element;

import core.exceptions.FileReadException;
import core.exceptions.FormatNotSupportedException;
import datatypes.DataType;

public interface DataReader {
	public Map<String, String[]> getValidReadFormats();
	public DataType read(String format, Scanner s) throws FormatNotSupportedException, FileReadException;
	public DataType readXML(Element e) throws FormatNotSupportedException, FileReadException;
}
