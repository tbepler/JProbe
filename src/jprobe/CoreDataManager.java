package jprobe;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.osgi.framework.Bundle;

import jprobe.services.CoreEvent;
import jprobe.services.CoreEvent.Type;
import jprobe.services.CoreListener;
import jprobe.services.Data;
import jprobe.services.DataManager;
import jprobe.services.DataReader;
import jprobe.services.DataWriter;
import jprobe.services.JProbeCore;

public class CoreDataManager implements DataManager{
	
	private JProbeCore core;
	
	private Collection<CoreListener> listeners;
	
	private Map<Class<? extends Data>, List<Data>> data;
	private Map<String, Data> nameToData;
	private Map<Data, String> dataToName;
	private Map<Class<? extends Data>, Integer> counts;
	private Map<Class<? extends Data>, DataReader> typeToReader;
	private Map<DataReader, Class<? extends Data>> readerToType;
	private Map<Class<? extends Data>, DataWriter> typeToWriter;
	private Map<DataWriter, Class<? extends Data>> writerToType;
	
	public CoreDataManager(JProbeCore core){
		this.core = core;
		listeners = new HashSet<CoreListener>();
		data = new HashMap<Class<? extends Data>, List<Data>>();
		nameToData = new HashMap<String, Data>();
		dataToName = new HashMap<Data, String>();
		counts = new HashMap<Class<? extends Data>, Integer>();
		typeToReader = new HashMap<Class<? extends Data>, DataReader>();
		readerToType = new HashMap<DataReader, Class<? extends Data>>();
		typeToWriter = new HashMap<Class<? extends Data>, DataWriter>();
		writerToType = new HashMap<DataWriter, Class<? extends Data>>();
	}
	
	@Override
	public void addListener(CoreListener listener){
		listeners.add(listener);
	}
	
	@Override
	public void removeListener(CoreListener listener){
		listeners.remove(listener);
	}
	
	private void notifyListeners(CoreEvent event){
		for(CoreListener l : listeners){
			l.update(event);
		}
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
	
	public void addData(Data d, String name, Bundle responsible){
		Class<? extends Data> clazz = d.getClass();
		if(!data.containsKey(clazz)){
			List<Data> list = new ArrayList<Data>();
			list.add(d);
			data.put(clazz, list);
			counts.put(clazz, 1);
		}else{
			List<Data> list = data.get(clazz);
			if(!list.contains(d)){
				list.add(d);
				counts.put(clazz, counts.get(clazz)+1);
			}
		}
		if(dataToName.containsKey(d)){
			this.rename(d, name, responsible);
		}else{
			nameToData.put(name, d);
			dataToName.put(d, name);
			notifyListeners(new CoreEvent(core, Type.DATA_ADDED, responsible, d));
		}
	}
	
	@Override
	public void addData(Data d, Bundle responsible){
		addData(d, assignName(d), responsible);
	}
	
	private void removeData(String name, Data d, Bundle responsible){
		data.get(d.getClass()).remove(d);
		nameToData.remove(name);
		dataToName.remove(d);
		notifyListeners(new CoreEvent(core, Type.DATA_REMOVED, responsible, d));
	}
	
	@Override
	public void removeData(String name, Bundle responsible){
		removeData(name, nameToData.get(name), responsible);
	}
	
	@Override
	public void removeData(Data d, Bundle responsible){
		removeData(dataToName.get(d), d, responsible);
	}
	
	@Override
	public List<Data> getAllData(){
		List<Data> full = new ArrayList<Data>();
		for(List<Data> part : data.values()){
			full.addAll(part);
		}
		return full;
	}
	
	@Override
	public List<Data> getData(Class<? extends Data> type){
		return Collections.unmodifiableList(data.get(type));
	}
	
	@Override
	public Data getData(String name){
		return nameToData.get(name);
	}
	
	@Override
	public String getDataName(Data d){
		return dataToName.get(d);
	}
	
	@Override
	public String[] getDataNames(){
		return nameToData.keySet().toArray(new String[nameToData.size()]);
	}
	
	@Override
	public void rename(Data d, String name, Bundle responsible){
		String old = dataToName.get(d);
		nameToData.remove(old);
		nameToData.put(name, d);
		dataToName.put(d, name);
		notifyListeners(new CoreEvent(core, Type.DATA_NAME_CHANGE, responsible, d));
	}
	
	@Override
	public boolean isReadable(Class<? extends Data> type){
		return typeToReader.containsKey(type);
	}
	
	@Override
	public boolean isWritable(Class<? extends Data> type){
		return typeToWriter.containsKey(type);
	}
	
	@Override
	public void addDataReader(Class<? extends Data> type, DataReader reader, Bundle responsible){
		typeToReader.put(type, reader);
		readerToType.put(reader, type);
		notifyListeners(new CoreEvent(core, Type.DATAREADER_ADDED, responsible, type));
	}
	
	@Override
	public void addDataWriter(Class<? extends Data> type, DataWriter writer, Bundle responsible){
		typeToWriter.put(type, writer);
		writerToType.put(writer, type);
		notifyListeners(new CoreEvent(core, Type.DATAWRITER_ADDED, responsible, type));
	}
	
	private void removeDataReader(Class<? extends Data> type, DataReader reader, Bundle responsible){
		readerToType.remove(reader);
		typeToReader.remove(type);
		notifyListeners(new CoreEvent(core, Type.DATAREADER_REMOVED, responsible, type));
	}
	
	private void removeDataWriter(Class<? extends Data> type, DataWriter writer, Bundle responsible){
		writerToType.remove(writer);
		typeToWriter.remove(type);
		notifyListeners(new CoreEvent(core, Type.DATAWRITER_REMOVED, responsible, type));
	}
	
	@Override
	public void removeDataReader(Class<? extends Data> type, Bundle responsible){
		removeDataReader(type, typeToReader.get(type), responsible);
	}
	
	@Override
	public void removeDataWriter(Class<? extends Data> type, Bundle responsible){
		removeDataWriter(type, typeToWriter.get(type), responsible);
	}
	
	@Override
	public void removeDataReader(DataReader reader, Bundle responsible){
		removeDataReader(readerToType.get(reader), reader, responsible);
	}
	
	@Override
	public void removeDataWriter(DataWriter writer, Bundle responsible){
		removeDataWriter(writerToType.get(writer), writer, responsible);
	}
	
	@Override
	public Collection<Class<? extends Data>> getReadableDataTypes(){
		return typeToReader.keySet();
	}
	
	@Override
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

	@Override
	public String[] getValidReadFormats(Class<? extends Data> type) {
		DataReader reader = typeToReader.get(type);
		if(reader == null){
			return new String[]{};
		}
		Map<String, String[]> formats = reader.getValidReadFormats();
		return formats.keySet().toArray(new String[formats.size()]);
	}

	@Override
	public String[] getValidWriteFormats(Class<? extends Data> type) {
		DataWriter writer = typeToWriter.get(type);
		if(writer == null){
			return new String[]{};
		}
		Map<String, String[]> formats = writer.getValidWriteFormats();
		return formats.keySet().toArray(new String[formats.size()]);
	}

	@Override
	public Data readData(File file, Class<? extends Data> type, String format, Bundle responsible) throws Exception {
		if(!this.isReadable(type)){
			throw new Exception(type+" not readable");
		}
		DataReader reader = typeToReader.get(type);
		if(reader == null){
			throw new Exception(type+" reader is null");
		}
		try{
			Data read = reader.read(format, new Scanner(file));
			this.addData(read, responsible);
			return read;
		} catch(Exception e){
			throw e;
		}
	}

	@Override
	public void writeData(File file, Data data, String format) throws Exception {
		if(!this.isWritable(data.getClass())){
			throw new Exception(data.getClass()+" not writable");
		}
		DataWriter writer = typeToWriter.get(data.getClass());
		if(writer == null){
			throw new Exception(data.getClass()+" writer is null");
		}
		try{
			writer.write(data, format, new BufferedWriter(new FileWriter(file)));
		} catch(Exception e){
			throw e;
		}
	}
	
	
	
	
	
	
	
	
}
