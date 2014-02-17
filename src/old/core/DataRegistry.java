package old.core;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

import old.datatypes.DataType;
import old.readwrite.DataReader;
import old.readwrite.DataWriter;

import org.xml.sax.SAXException;

import util.ExtensionUtils;
import util.XMLParser;

public class DataRegistry {
	
	private Map<String, DataReader> readers;
	private Map<String, DataWriter> writers;
	private Map<String, Class<? extends DataType>> dataTypes;
	
	public DataRegistry(String directory, String registry) throws URISyntaxException, IOException, SAXException, ClassNotFoundException, InstantiationException, IllegalAccessException{
		readers = new HashMap<String, DataReader>();
		writers = new HashMap<String, DataWriter>();
		dataTypes = new HashMap<String, Class<? extends DataType>>();
		
		ClassLoader loader = ExtensionUtils.createDirectoryLoader(directory);
		XMLParser parser = new XMLParser(registry);
		
		List<String[]> entries = parser.parseDataTypes();
		for(String[] data : entries){
			dataTypes.put(data[0], (Class<? extends DataType>) loader.loadClass(data[1]));
			if(!data[2].equals("")){
				Class<? extends DataReader> clazz = (Class<? extends DataReader>) loader.loadClass(data[2]);
				readers.put(data[0], clazz.newInstance());
			}
			if(!data[3].equals("")){
				Class<? extends DataWriter> clazz = (Class<? extends DataWriter>) loader.loadClass(data[3]);
				writers.put(data[0], clazz.newInstance());
			}
		}
	}
	
	public Collection<String> getReaderIds(){
		return new ArrayList<String>(readers.keySet());
	}
	
	public Collection<String> getWriterIds(){
		return new ArrayList<String>(writers.keySet());
	}
	
	public Collection<String> getDataTypeIds(){
		return new ArrayList<String>(dataTypes.keySet());
	}
	
	public Collection<DataReader> getReaders(){
		return new ArrayList<DataReader>(readers.values());
	}
	
	public DataReader getReader(String id){
		return readers.get(id);
	}
	
	public Collection<DataWriter> getWriters(){
		return new ArrayList<DataWriter>(writers.values());
	}
	
	public DataWriter getWriter(String id){
		return writers.get(id);
	}
	
	public String getId(Class<? extends DataType> clazz){
		for(String id : dataTypes.keySet()){
			if(dataTypes.get(id).equals(clazz)){
				return id;
			}
		}
		return "";
	}
	
	
	
	
	
	
	
	
	
	
	
	
}
