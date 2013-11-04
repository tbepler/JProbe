package readwrite;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import datatypes.*;
import datatypes.location.GenomeLocation;
import datatypes.location.Location;
import datatypes.sequence.Sequence;
import exceptions.FileReadException;
import exceptions.FormatNotSupportedException;
import exceptions.InvalidClassException;


public class TragedyFileReader {
	
	public static final String WHITESPACE_REGEX = "\\s+";
	
	private static final FileFormat[] PEAK_READ_FORMATS = new FileFormat[]{FileFormat.BED, FileFormat.ENCODEPEAK, FileFormat.XML};
	
	public FileFormat[] getPeakReadFormats(){
		return PEAK_READ_FORMATS;
	}
	
	public Group<Peak> readPeakGroup(String file, FileFormat format) throws FormatNotSupportedException, FileReadException, FileNotFoundException{
		switch(format){
			case BED:
				return readPeakGroup(new Scanner(new File(file)));
			case ENCODEPEAK:
				return readPeakGroup(new Scanner(new File(file)));
			case XML:
				try{
					DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
					DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
					Document doc = docBuilder.parse (new File(file));
					return readPeakGroup(doc);
				} catch (Exception e){
					throw new FileReadException(e);
				}
			default:
				throw new FormatNotSupportedException(format.getName());
		}
	}
	
	public Group<Peak> readPeakGroup(Scanner s) throws FileReadException{
		Group<Peak> peaks = new Group<Peak>();
		while(s.hasNextLine()){
			peaks.add(readPeak(s.nextLine()));
		}
		return peaks;
	}
	
	public Group<Peak> readPeakGroup(Document doc) throws FileReadException{
		Group<Peak> peaks = new Group<Peak>();
		doc.getDocumentElement ().normalize ();
		NodeList peakList = doc.getElementsByTagName("peak");
		for(int i=0; i<peakList.getLength(); i++){
			Element p = (Element) peakList.item(i);
			try{
				peaks.add(readPeak(p));
			}catch(Exception e){
				throw new FileReadException(e);
			}
		}
		return peaks;
	}
	
	public Peak readPeak(String line) throws FileReadException{
		return readPeak(line.split(WHITESPACE_REGEX));
	}

	public Peak readPeak(String[] entries) throws FileReadException{
		try{
			GenomeLocation loc = new GenomeLocation(entries[0], Integer.parseInt(entries[1]), Integer.parseInt(entries[2]));
			return new Peak(loc);
		}catch(Exception e){
			throw new FileReadException("Invalid entry format");
		}
	}
	
	public Peak readPeak(Element p) throws FileReadException{
		Element seq = (Element) p.getElementsByTagName("sequence").item(0);
		Element loc = (Element) p.getElementsByTagName("location").item(0);
		int id = Integer.parseInt(p.getElementsByTagName("id").item(0).getTextContent().trim());
		try{
			Peak peak = new Peak(readLocation(loc), readSequence(seq));
			peak.setId(id);
			return peak;
		}catch(Exception e){
			
		}
		try{
			Peak peak = new Peak(readLocation(loc));
			peak.setId(id);
			return peak;
		}catch(Exception e){
			throw new FileReadException(e);
		}
	}
	
	public Sequence readSequence(Element e) throws ClassNotFoundException, InvalidClassException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		String classpath = e.getElementsByTagName("classpath").item(0).getTextContent().trim();
		Class<?> clazz = Class.forName(classpath);
		if(Sequence.class.isAssignableFrom(clazz)){
			Method read = clazz.getMethod("readXML", Element.class);
			return (Sequence) read.invoke(null, e);
		}
		throw new InvalidClassException(classpath+" is not a valid Sequence object");
		
	}
	
	public Location readLocation(Element e) throws InvalidClassException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException{
		String classpath = e.getElementsByTagName("classpath").item(0).getTextContent().trim();
		Class<?> clazz = Class.forName(classpath);
		if(Location.class.isAssignableFrom(clazz)){
			Method read = clazz.getMethod("readXML", Element.class);
			return (Location) read.invoke(null, e);
		}
		throw new InvalidClassException(classpath+" is not a valid Location object");
		
	}
	
	
	
	
	
	
	
	
	
	
	
}
