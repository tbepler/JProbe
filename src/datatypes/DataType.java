package datatypes;

import java.io.BufferedWriter;
import java.util.Scanner;

import org.w3c.dom.Element;

import exceptions.FileReadException;
import exceptions.FormatNotSupportedException;

public interface DataType{
	
	public static interface Factory{
		public String[] getValidReadFormats();
		public DataType read(String format, Scanner s) throws FormatNotSupportedException, FileReadException;
		public DataType readXML(Element e) throws FormatNotSupportedException, FileReadException;
		public String[] getValidWriteFormats();
		public void write(String format, BufferedWriter out) throws FormatNotSupportedException;
	}
	
	public int getId();
	public void setId(int id);
	public String getName();
	
}
