package jprobe;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.swing.filechooser.FileNameExtensionFilter;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

import utils.ClassLoaderObjectInputStream;
import utils.OSGIUtils;
import jprobe.services.CoreEvent;
import jprobe.services.CoreEvent.Type;
import jprobe.services.data.Data;
import jprobe.services.data.DataReader;
import jprobe.services.data.DataWriter;
import jprobe.services.CoreListener;
import jprobe.services.DataManager;
import jprobe.services.ErrorHandler;
import jprobe.services.JProbeCore;
import jprobe.services.Saveable;

public class CoreDataManager implements DataManager, Saveable{
	
	private JProbeCore m_Core;
	
	private Collection<CoreListener> m_Listeners;
	
	private BundleContext m_Context;
	private Map<Class<? extends Data>, List<Data>> m_Data;
	private Map<Class<? extends Data>, String> m_DataProviders;
	private Map<String, Data> m_NameToData;
	private Map<Data, String> m_DataToName;
	private Map<Class<? extends Data>, Integer> m_Counts;
	private Map<Class<? extends Data>, DataReader> m_TypeToReader;
	private Map<DataReader, Class<? extends Data>> m_ReaderToType;
	private Map<Class<? extends Data>, DataWriter> m_TypeToWriter;
	private Map<DataWriter, Class<? extends Data>> m_WriterToType;
	private boolean m_ChangesSinceLastSave;
	
	public CoreDataManager(JProbeCore core, BundleContext context){
		m_Core = core;
		m_Context = context;
		m_Listeners = new HashSet<CoreListener>();
		m_Data = new HashMap<Class<? extends Data>, List<Data>>();
		m_DataProviders = new HashMap<Class<? extends Data>, String>();
		m_NameToData = new HashMap<String, Data>();
		m_DataToName = new HashMap<Data, String>();
		m_Counts = new HashMap<Class<? extends Data>, Integer>();
		m_TypeToReader = new HashMap<Class<? extends Data>, DataReader>();
		m_ReaderToType = new HashMap<DataReader, Class<? extends Data>>();
		m_TypeToWriter = new HashMap<Class<? extends Data>, DataWriter>();
		m_WriterToType = new HashMap<DataWriter, Class<? extends Data>>();
		m_ChangesSinceLastSave = false;
	}
	
	public void setBundleContext(BundleContext context){
		m_Context = context;
	}
	
	@Override
	public void addListener(CoreListener listener){
		m_Listeners.add(listener);
	}
	
	@Override
	public void removeListener(CoreListener listener){
		m_Listeners.remove(listener);
	}
	
	private void notifyListeners(CoreEvent event){
		for(CoreListener l : m_Listeners){
			m_ChangesSinceLastSave = true;
			l.update(event);
		}
	}
	
	private String assignName(Data d){
		int count;
		if(m_Counts.containsKey(d)){
			count = m_Counts.get(d.getClass()) + 1;
		}else{
			count = 1;
		}
		String name = d.getClass().getSimpleName()+String.valueOf(count);
		while(m_NameToData.containsKey(name)){
			name = d.getClass().getSimpleName()+String.valueOf(++count);
		}
		return name;
	}
	
	public void addData(Data d, String name, Bundle responsible){
		Class<? extends Data> clazz = d.getClass();
		if(!m_Data.containsKey(clazz)){
			List<Data> list = new ArrayList<Data>();
			list.add(d);
			m_Data.put(clazz, list);
			m_Counts.put(clazz, 1);
			m_DataProviders.put(clazz, OSGIUtils.getProvider(clazz, m_Context).getSymbolicName());
		}else{
			List<Data> list = m_Data.get(clazz);
			if(!list.contains(d)){
				list.add(d);
				m_Counts.put(clazz, m_Counts.get(clazz)+1);
			}
		}
		if(m_DataToName.containsKey(d)){
			this.rename(d, name, responsible);
		}else{
			m_NameToData.put(name, d);
			m_DataToName.put(d, name);
			notifyListeners(new CoreEvent(m_Core, Type.DATA_ADDED, responsible, d));
		}
	}
	
	@Override
	public void addData(Data d, Bundle responsible){
		addData(d, assignName(d), responsible);
	}
	
	private void removeData(String name, Data d, Bundle responsible){
		m_Data.get(d.getClass()).remove(d);
		m_NameToData.remove(name);
		m_DataToName.remove(d);
		notifyListeners(new CoreEvent(m_Core, Type.DATA_REMOVED, responsible, d));
	}
	
	@Override
	public void removeData(String name, Bundle responsible){
		removeData(name, m_NameToData.get(name), responsible);
	}
	
	@Override
	public void removeData(Data d, Bundle responsible){
		removeData(m_DataToName.get(d), d, responsible);
	}
	
	@Override
	public List<Data> getAllData(){
		List<Data> full = new ArrayList<Data>();
		for(List<Data> part : m_Data.values()){
			full.addAll(part);
		}
		return full;
	}
	
	@Override
	public List<Data> getData(Class<? extends Data> type){
		return Collections.unmodifiableList(m_Data.get(type));
	}
	
	@Override
	public Data getData(String name){
		return m_NameToData.get(name);
	}
	
	@Override
	public String getDataName(Data d){
		return m_DataToName.get(d);
	}
	
	@Override
	public String[] getDataNames(){
		return m_NameToData.keySet().toArray(new String[m_NameToData.size()]);
	}
	
	@Override
	public void rename(Data d, String name, Bundle responsible){
		String old = m_DataToName.get(d);
		if(m_NameToData.containsKey(name)){
			this.removeData(name, responsible);
		}
		m_NameToData.remove(old);
		m_NameToData.put(name, d);
		m_DataToName.put(d, name);
		notifyListeners(new CoreEvent(m_Core, Type.DATA_NAME_CHANGE, responsible, d, old, name));
	}
	
	@Override
	public boolean isReadable(Class<? extends Data> type){
		return m_TypeToReader.containsKey(type);
	}
	
	@Override
	public boolean isWritable(Class<? extends Data> type){
		return m_TypeToWriter.containsKey(type);
	}
	
	@Override
	public void addDataReader(Class<? extends Data> type, DataReader reader, Bundle responsible){
		m_TypeToReader.put(type, reader);
		m_ReaderToType.put(reader, type);
		notifyListeners(new CoreEvent(m_Core, Type.DATAREADER_ADDED, responsible, type));
	}
	
	@Override
	public void addDataWriter(Class<? extends Data> type, DataWriter writer, Bundle responsible){
		m_TypeToWriter.put(type, writer);
		m_WriterToType.put(writer, type);
		notifyListeners(new CoreEvent(m_Core, Type.DATAWRITER_ADDED, responsible, type));
	}
	
	private void removeDataReader(Class<? extends Data> type, DataReader reader, Bundle responsible){
		m_ReaderToType.remove(reader);
		m_TypeToReader.remove(type);
		notifyListeners(new CoreEvent(m_Core, Type.DATAREADER_REMOVED, responsible, type));
	}
	
	private void removeDataWriter(Class<? extends Data> type, DataWriter writer, Bundle responsible){
		m_WriterToType.remove(writer);
		m_TypeToWriter.remove(type);
		notifyListeners(new CoreEvent(m_Core, Type.DATAWRITER_REMOVED, responsible, type));
	}
	
	@Override
	public void removeDataReader(Class<? extends Data> type, Bundle responsible){
		removeDataReader(type, m_TypeToReader.get(type), responsible);
	}
	
	@Override
	public void removeDataWriter(Class<? extends Data> type, Bundle responsible){
		removeDataWriter(type, m_TypeToWriter.get(type), responsible);
	}
	
	@Override
	public void removeDataReader(DataReader reader, Bundle responsible){
		removeDataReader(m_ReaderToType.get(reader), reader, responsible);
	}
	
	@Override
	public void removeDataWriter(DataWriter writer, Bundle responsible){
		removeDataWriter(m_WriterToType.get(writer), writer, responsible);
	}
	
	@Override
	public Collection<Class<? extends Data>> getReadableDataTypes(){
		return m_TypeToReader.keySet();
	}
	
	@Override
	public Collection<Class<? extends Data>> getWritableDataTypes(){
		return m_TypeToWriter.keySet();
	}
	
	public DataReader getReader(Class<? extends Data> type){
		return m_TypeToReader.get(type);
	}
	
	public DataWriter getWriter(Class<? extends Data> type){
		return m_TypeToWriter.get(type);
	}
	
	public Class<? extends Data> getReadType(DataReader reader){
		return m_ReaderToType.get(reader);
	}
	
	public Class<? extends Data> getWriteType(DataWriter writer){
		return m_WriterToType.get(writer);
	}

	@Override
	public FileNameExtensionFilter[] getValidReadFormats(Class<? extends Data> type) {
		DataReader reader = m_TypeToReader.get(type);
		if(reader == null){
			return new FileNameExtensionFilter[]{};
		}
		return reader.getValidReadFormats();
	}

	@Override
	public FileNameExtensionFilter[] getValidWriteFormats(Class<? extends Data> type) {
		DataWriter writer = m_TypeToWriter.get(type);
		if(writer == null){
			return new FileNameExtensionFilter[]{};
		}
		return writer.getValidWriteFormats();
	}

	@Override
	public Data readData(File file, Class<? extends Data> type, FileNameExtensionFilter format, Bundle responsible) throws Exception {
		if(!this.isReadable(type)){
			throw new Exception(type+" not readable");
		}
		DataReader reader = m_TypeToReader.get(type);
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
	public void writeData(File file, Data data, FileNameExtensionFilter format) throws Exception {
		if(!this.isWritable(data.getClass())){
			throw new Exception(data.getClass()+" not writable");
		}
		DataWriter writer = m_TypeToWriter.get(data.getClass());
		if(writer == null){
			throw new Exception(data.getClass()+" writer is null");
		}
		try{
			BufferedWriter buffer = new BufferedWriter(new FileWriter(file));
			writer.write(data, format, buffer);
			buffer.close();
		} catch(Exception e){
			throw e;
		}
	}

	@Override
	public boolean contains(String name) {
		return m_NameToData.containsKey(name);
	}

	@Override
	public boolean contains(Data data) {
		return m_DataToName.containsKey(data);
	}
	
	private void clearData(){
		for(Data stored : this.getAllData()){
			this.removeData(stored, JProbeActivator.getBundle());
		}
	}
	
	@Override
	public boolean changesSinceLastSave(){
		return m_ChangesSinceLastSave;
	}

	@Override
	public void save(OutputStream out) {
		try {
			ObjectOutputStream oout = new ObjectOutputStream(out);
			for(Data stored : this.getAllData()){
				String name = this.getDataName(stored);
				String bundle = m_DataProviders.get(stored.getClass());
				oout.writeObject(name);
				oout.writeObject(bundle);
				oout.writeObject(stored);
			}
			oout.close();
		} catch (IOException e) {
			ErrorHandler.getInstance().handleException(e, JProbeActivator.getBundle());
		}
	}

	@Override
	public void load(InputStream in) {
		try {
			this.clearData();
			ClassLoaderObjectInputStream oin = new ClassLoaderObjectInputStream(in, this.getClass().getClassLoader());
			boolean finished = false;
			while(!finished){
				try {
					oin.setClassLoader(this.getClass().getClassLoader());
					String name = (String) oin.readObject();
					String bundleName = (String) oin.readObject();
					Bundle bundle = OSGIUtils.getBundleWithName(bundleName, m_Context);
					if(bundle!=null){
						oin.setClassLoader(OSGIUtils.getBundleClassLoader(bundle));
					}
					Data data = (Data) oin.readObject();
					this.addData(data, name, JProbeActivator.getBundle());
				} catch (ClassNotFoundException e) {
					//do nothing, this means the plugin that provides the data type is not loaded so simply proceed
					continue;
				} catch (Exception e){
					finished = true;
				}
			}
			oin.close();
		} catch (IOException e) {
			ErrorHandler.getInstance().handleException(e, JProbeActivator.getBundle());
		}
	}
	
	
	
	
	
	
	
	
}
