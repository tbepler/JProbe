package datatypes;

import java.io.BufferedWriter;
import java.util.Scanner;

import org.w3c.dom.Element;

import core.Constants;

import datatypes.sequence.Sequence;
import datatypes.location.*;
import exceptions.FileReadException;
import exceptions.FormatNotSupportedException;

/**
 * This class is used to represent peak objects. It contains the chromosome, start and ending locations for the peak and can have a sequence associated with it.
 * 
 * @author Tristan Bepler
 * @see Location, Sequence, DataType
 */

public class Peak implements Location, Sequence, DataType{
	
	public static final String[] PEAK_READ_FORMATS = new String[]{"bed", "encode", "xml"}; 
	public static final String[] PEAK_WRITE_FORMATS = new String[]{"bed", "encode", "xml"};
	public static final String BED_ENCODE_LINE_FORMAT_REGEX = "^(chr).+\\s+\\d+\\s+\\d+.*$";
	
	private Sequence seq = null;
	private Location loc;
	private int id;
	
	Factory f = new Factory(){
		
		@Override
		public String[] getValidReadFormats() {
			return PEAK_READ_FORMATS;
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
		public String[] getValidWriteFormats() {
			return PEAK_WRITE_FORMATS;
		}

		@Override
		public void write(String format, BufferedWriter out)
				throws FormatNotSupportedException {
			// TODO Auto-generated method stub
			
		}
		
	};
	
	public Peak(Location loc) {
		this.loc = loc;
	}
	
	public Peak(Location loc, Sequence seq){
		this(loc);
		this.seq = seq;
	}
	
	public void setSequence(Sequence seq){
		this.seq = seq;
	}
	
	@Override
	public String getChr(){
		return loc.getChr();
	}
	
	@Override
	public int getStart(){
		return loc.getStart();
	}
	
	@Override
	public int getEnd(){
		return loc.getEnd();
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public void setId(int id) {
		this.id = id;
	}

	@Override
	public Location parseLocation(String l) {
		return loc.parseLocation(l);
	}

	@Override
	public String locationToString() {
		return loc.locationToString();
	}

	@Override
	public String getSeq() {
		if(seq == null) return "";
		return seq.getSeq();
	}

	@Override
	public int length() {
		if(seq == null) return 0;
		return seq.length();
	}

	@Override
	public String getMutationFlag() {
		if(seq == null) return "";
		return seq.getMutationFlag();
	}

	@Override
	public String getOrientationFlag() {
		if(seq == null) return "";
		return seq.getOrientationFlag();
	}

	@Override
	public String seqToString() {
		if(seq == null) return "";
		return seq.seqToString();
	}

	@Override
	public String getName() {
		return "";
	}

}
