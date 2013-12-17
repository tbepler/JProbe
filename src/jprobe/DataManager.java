package jprobe;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jprobe.services.Data;
import jprobe.services.DataReader;
import jprobe.services.DataWriter;

public class DataManager {
	
	private Map<Class<? extends Data>, List<Data>> data;
	private Map<String, Data> nameToData;
	private Map<Data, String> dataToName;
	private Map<Class<? extends Data>, Integer> counts;
	private Map<Class<? extends Data>, DataReader> typeToReader;
	private Map<DataReader, Class<? extends Data>> readerToType;
	private Map<Class<? extends Data>, DataWriter> typeToWriter;
	private Map<DataWriter, Class<? extends Data>> writerToType;
	
	public DataManager(){
		data = new HashMap<Class<? extends Data>, List<Data>>();
		nameToData = new HashMap<String, Data>();
		dataToName = new HashMap<Data, String>();
		counts = new HashMap<Class<? extends Data>, Integer>();
		typeToReader = new HashMap<Class<? extends Data>, DataReader>();
		readerToType = new HashMap<DataReader, Class<? extends Data>>();
		typeToWriter = new HashMap<Class<? extends Data>, DataWriter>();
		writerToType = new HashMap<DataWriter, Class<? extends Data>>();
	}
	
	private String assignName(Data d){
		int count;
		if(counts.containsKey(d)){
			count = counts.get(d.getClass()) + 1;
		}else{
			count = 1;
		}
		String name = d.getClass().getSimpleName()+String.valueOf(count);
		while(nameToData.containsKey(name)){
			name = d.getClass().getSimpleName()+String.valueOf(++count);
		}
		return name;
	}
	
	public void addData(Data d, String name){
		Class<? extends Data> clazz = d.getClass();
		if(!data.containsKey(clazz)){
			List<Data> list = new ArrayList<Data>();
			list.add(d);
			data.put(clazz, list);
			counts.put(clazz, 1);
		}else{
			data.get(clazz).add(d);
			counts.put(clazz, counts.get(clazz)+1);
		}
		nameToData.put(name, d);
		dataToName.put(d, name);
	}
	
	public void addData(Data d){
		addData(d, assignName(d));
	}
	
	private void removeData(String name, Data d){
		data.get(d.getClass()).remove(d);
		nameToData.remove(name);
		dataToName.remove(d);
	}
	
	public void removeData(String name){
		removeData(name, nameToData.get(name));
	}
	
	public void removeData(Data d){
		removeData(dataToName.get(d), d);
	}
	
	public List<Data> getAllData(){
		List<Data> full = new ArrayList<Data>();
		for(List<Data> part : data.values()){
			full.addAll(part);
		}
		return full;
	}
	
	public List<Data> getData(Class<? extends Data> type){
		return Collections.unmodifiableList(data.get(type));
	}
	
	public Data getData(String name){
		return nameToData.get(name);
	}
	
	public String getDataName(Data d){
		return dataToName.get(d);
	}
	
	public void rename(Data d, String name){
		String old = dataToName.get(d);
		nameToData.remove(old);
		nameToData.put(name, d);
		dataToName.put(d, name);
	}
	
	public boolean isReadable(Class<? extends Data> type){
		return typeToReader.containsKey(type);
	}
	
	public boolean isWritable(Class<? extends Data> type){
		return typeToWriter.containsKey(type);
	}
	
	public void addDataReader(Class<? extends Data> type, DataReader reader){
		typeToReader.put(type, reader);
		readerToType.put(reader, type);
	}
	
	public void addDataWriter(Class<? extends Data> type, DataWriter writer){
		typeToWriter.put(type, writer);
		writerToType.put(writer, type);
	}
	
	public void removeDataReader(Class<? extends Data> type){
		DataReader reader = typeToReader.get(type);
		readerToType.remove(reader);
		typeToReader.remove(type);
	}
	
	public void removeDataWriter(Class<? extends Data> type){
		DataWriter writer = typeToWriter.get(type);
		writerToType.remove(writer);
		typeToWriter.remove(type);
	}
	
	public void removeDataReader(DataReader reader){
		Class<? extends Data> type = readerToType.get(reader);
		typeToReader.remove(type);
		readerToType.remove(reader);
	}
	
	public void removeDataWriter(DataWriter writer){
		Class<? extends Data> type = writerToType.get(writer);
		typeToWriter.remove(type);
		writerToType.remove(writer);
	}
	
	public Collection<Class<? extends Data>> getReadableDataTypes(){
		return typeToReader.keySet();
	}
	
	public Collection<Class<? extends Data>> getWritableDataTypes(){
		return typeToWriter.keySet();
	}
	
	public DataReader getReader(Class<? extends Data> type){
		return typeToReader.get(type);
	}
	
	public DataWriter getWriter(Class<? extends Data> type){
		return typeToWriter.get(type);
	}
	
	public Class<? extends Data> getReadType(DataReader reader){
		return readerToType.get(reader);
	}
	
	public Class<? extends Data> getWriteType(DataWriter writer){
		return writerToType.get(writer);
	}
	
	
	
	
	
	
	
	
}
