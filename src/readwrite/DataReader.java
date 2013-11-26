package readwrite;

import java.util.Map;
import java.util.Scanner;

import org.w3c.dom.Element;

import datatypes.DataType;
import exceptions.FileReadException;
import exceptions.FormatNotSupportedException;

public interface DataReader {
	public Map<String, String[]> getValidReadFormats();
	public DataType read(String format, Scanner s) throws FormatNotSupportedException, FileReadException;
	public DataType readXML(Element e) throws FormatNotSupportedException, FileReadException;
}
