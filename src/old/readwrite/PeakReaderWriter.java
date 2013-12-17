package old.readwrite;

import java.io.BufferedWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import old.core.Constants;
import old.core.exceptions.FileReadException;
import old.core.exceptions.FormatNotSupportedException;
import old.datatypes.DataType;
import old.datatypes.Peak;
import old.datatypes.location.GenomeLocation;

import org.w3c.dom.Element;

public class PeakReaderWriter implements DataReader, DataWriter{
	
	public static final String[] PEAK_READ_FORMATS = new String[]{"bed", "encode", "xml"}; 
	public static final String[] PEAK_WRITE_FORMATS = new String[]{"bed", "encode", "xml"};
	public static final String BED_ENCODE_LINE_FORMAT_REGEX = "^(chr).+\\s+\\d+\\s+\\d+.*$";
	
	@Override
	public Map<String, String[]> getValidReadFormats() {
		Map<String, String[]> formats = new HashMap<String, String[]>();
		formats.put("bed", new String[]{});
		formats.put("encode", new String[]{});
		formats.put("xml", new String[]{".xml"});
		return formats;
	}

	@Override
	public DataType read(String format, Scanner s) throws FormatNotSupportedException, FileReadException {
		if(format.equalsIgnoreCase("bed")||format.equalsIgnoreCase("encode")){
			if(s.hasNextLine()){
				String line = s.nextLine();
				if(line.matches(BED_ENCODE_LINE_FORMAT_REGEX)){
					return readPeak(line);
				}
			}
		}
		throw new FormatNotSupportedException(format+" read not supported by this DataType.");
	}
	
	private Peak readPeak(String line) throws FileReadException{
		return readPeak(line.split(Constants.WHITESPACE_REGEX));
	}

	private Peak readPeak(String[] entries) throws FileReadException{
		try{
			GenomeLocation loc = new GenomeLocation(entries[0], Integer.parseInt(entries[1]), Integer.parseInt(entries[2]));
			return new Peak(loc);
		}catch(Exception e){
			throw new FileReadException("Invalid entry format");
		}
	}

	@Override
	public DataType readXML(Element e) throws FormatNotSupportedException, FileReadException {
		// TODO Auto-generated method stub
		return null;
	}
	

	@Override
	public Map<String, String[]> getValidWriteFormats() {
		Map<String, String[]> formats = new HashMap<String, String[]>();
		formats.put("bed", new String[]{});
		formats.put("encode", new String[]{});
		formats.put("xml", new String[]{".xml"});
		return formats;
	}

	@Override
	public void write(DataType data, String format, BufferedWriter out)
			throws FormatNotSupportedException {
		// TODO Auto-generated method stub
		
	}
	
}
