package jprobe.services;

import java.io.File;
import java.util.Collection;
import java.util.List;

import javax.swing.filechooser.FileNameExtensionFilter;

import jprobe.services.data.Data;
import jprobe.services.data.DataReader;
import jprobe.services.data.DataWriter;

import org.osgi.framework.Bundle;

public interface DataManager {
	
	public void addListener(CoreListener listener);
	public void removeListener(CoreListener listener);
	
	public void addDataReader(Class<? extends Data> read, DataReader reader, Bundle responsible);
	public void removeDataReader(DataReader reader, Bundle responsible);
	public void removeDataReader(Class<? extends Data> type, Bundle responsible);
	public Collection<Class<? extends Data>> getReadableDataTypes();
	public FileNameExtensionFilter[] getValidReadFormats(Class<? extends Data> type);
	
	public void addDataWriter(Class<? extends Data> write, DataWriter writer, Bundle responsible);
	public void removeDataWriter(DataWriter writer, Bundle responsible);
	public void removeDataWriter(Class<? extends Data> type, Bundle responsible);
	public Collection<Class<? extends Data>> getWritableDataTypes();
	public FileNameExtensionFilter[] getValidWriteFormats(Class<? extends Data> type);
	
	public Data readData(File file, Class<? extends Data> type, FileNameExtensionFilter format, Bundle responsible) throws Exception;
	public void writeData(File file, Data data, FileNameExtensionFilter format) throws Exception;
	public boolean isReadable(Class<? extends Data> type);
	public boolean isWritable(Class<? extends Data> type);
	
	public void addData(Data data, Bundle responsible);
	public void removeData(Data data, Bundle responsible);
	public void removeData(String name, Bundle responsible);
	public List<Data> getAllData();
	public String[] getDataNames();
	public void rename(Data data, String name, Bundle responsible);
	public String getDataName(Data data);
	public Data getData(String name);
	public boolean contains(String name);
	public boolean contains(Data data);
	public List<Data> getData(Class<? extends Data> type);
	
	public boolean changesSinceLastSave();
	
}
