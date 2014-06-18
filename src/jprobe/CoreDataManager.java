package jprobe;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

import util.ByteCounterOutputStream;
import util.ClassLoaderObjectInputStream;
import util.OSGIUtils;
import jprobe.services.CoreEvent;
import jprobe.services.CoreEvent.Type;
import jprobe.services.data.Data;
import jprobe.services.data.DataReader;
import jprobe.services.data.DataWriter;
import jprobe.services.AbstractServiceListener;
import jprobe.services.CoreListener;
import jprobe.services.DataManager;
import jprobe.services.ErrorHandler;
import jprobe.services.JProbeCore;

public class CoreDataManager implements DataManager{
	
	private final Collection<CoreListener> m_Listeners = new HashSet<CoreListener>();

	private final Map<Class<? extends Data>, List<Data>> m_Data = new HashMap<Class<? extends Data>, List<Data>>();
	private final Map<Class<? extends Data>, String> m_DataProviders = new HashMap<Class<? extends Data>, String>();
	private final Map<String, Data> m_NameToData = new HashMap<String, Data>();
	private final Map<Data, String> m_DataToName = new LinkedHashMap<Data, String>();
	private final Map<Class<? extends Data>, Integer> m_Counts = new HashMap<Class<? extends Data>, Integer>();
	private final Map<Class<? extends Data>, DataReader> m_TypeToReader = new HashMap<Class<? extends Data>, DataReader>();
	private final Map<DataReader, Class<? extends Data>> m_ReaderToType = new HashMap<DataReader, Class<? extends Data>>();
	private final Map<Class<? extends Data>, DataWriter> m_TypeToWriter = new HashMap<Class<? extends Data>, DataWriter>();
	private final Map<DataWriter, Class<? extends Data>> m_WriterToType = new HashMap<DataWriter, Class<? extends Data>>();
	

	private final JProbeCore m_Core;

	private final AbstractServiceListener<DataReader> m_ReaderListener;
	private final AbstractServiceListener<DataWriter> m_WriterListener;
	
	private boolean m_ChangesSinceLastSave = false;
	
	private BundleContext m_Context;
	
	public CoreDataManager(JProbeCore core, BundleContext context){
		m_Core = core;
		m_Context = context;
		m_ReaderListener = new AbstractServiceListener<DataReader>(DataReader.class, context){

			@Override
			public void register(DataReader service, Bundle provider) {
				addDataReader(service, provider);
			}

			@Override
			public void unregister(DataReader service, Bundle provider) {
				removeDataReader(service, provider);
			}
			
		};
		m_WriterListener = new AbstractServiceListener<DataWriter>(DataWriter.class, context){

			@Override
			public void register(DataWriter service, Bundle provider) {
				addDataWriter(service, provider);
			}

			@Override
			public void unregister(DataWriter service, Bundle provider) {
				removeDataWriter(service, provider);
			}
			
		};
	}
	
	public void setBundleContext(BundleContext context){
		m_Context = context;
	}
	
	@Override
	public synchronized void addListener(CoreListener listener){
		m_Listeners.add(listener);
	}
	
	@Override
	public synchronized void removeListener(CoreListener listener){
		m_Listeners.remove(listener);
	}
	
	private void notifyListeners(CoreEvent event){
		for(CoreListener l : m_Listeners){
			m_ChangesSinceLastSave = event.type() == CoreEvent.Type.DATA_ADDED
					|| event.type() == CoreEvent.Type.DATA_NAME_CHANGE 
					|| event.type() == CoreEvent.Type.DATA_REMOVED;
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
	
	private synchronized void addData(Data d, String name, Bundle responsible, boolean notify){
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
			if(notify){
				notifyListeners(new CoreEvent(m_Core, Type.DATA_ADDED, responsible, d));
			}
		}
	}
	
	public synchronized void addData(Data d, String name, Bundle responsible){
		this.addData(d, name, responsible, true);
	}
	
	@Override
	public synchronized void addData(Data d, Bundle responsible){
		addData(d, assignName(d), responsible);
	}
	
	private void removeData(String name, Data d, Bundle responsible){
		m_Data.get(d.getClass()).remove(d);
		m_NameToData.remove(name);
		m_DataToName.remove(d);
		d.dispose();
		notifyListeners(new CoreEvent(m_Core, Type.DATA_REMOVED, responsible, d));
	}
	
	@Override
	public synchronized void removeData(String name, Bundle responsible){
		removeData(name, m_NameToData.get(name), responsible);
	}
	
	@Override
	public synchronized void removeData(Data d, Bundle responsible){
		removeData(m_DataToName.get(d), d, responsible);
	}
	
	@Override
	public synchronized List<Data> getAllData(){
		List<Data> full = new ArrayList<Data>();
		for(Data d : m_DataToName.keySet()){
			full.add(d);
		}
		return full;
	}
	
	@Override
	public synchronized List<Data> getData(Class<? extends Data> type){
		return Collections.unmodifiableList(m_Data.get(type));
	}
	
	@Override
	public synchronized Data getData(String name){
		return m_NameToData.get(name);
	}
	
	@Override
	public synchronized String getDataName(Data d){
		return m_DataToName.get(d);
	}
	
	@Override
	public synchronized String[] getDataNames(){
		return m_NameToData.keySet().toArray(new String[m_NameToData.size()]);
	}
	
	@Override
	public synchronized void rename(Data d, String name, Bundle responsible){
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
	public synchronized boolean isReadable(Class<? extends Data> type){
		return m_TypeToReader.containsKey(type);
	}
	
	@Override
	public synchronized boolean isWritable(Class<? extends Data> type){
		return m_TypeToWriter.containsKey(type);
	}
	
	@Override
	public synchronized DataReader getDataReader(Class<? extends Data> type){
		return m_TypeToReader.get(type);
	}
	
	@Override
	public synchronized DataWriter getDataWriter(Class<? extends Data> type){
		return m_TypeToWriter.get(type);
	}
	
	@Override
	public synchronized void addDataReader(DataReader reader, Bundle responsible){
		m_TypeToReader.put(reader.getReadClass(), reader);
		m_ReaderToType.put(reader, reader.getReadClass());
		notifyListeners(new CoreEvent(m_Core, Type.DATAREADER_ADDED, responsible, reader.getReadClass()));
	}
	
	@Override
	public synchronized void addDataWriter(DataWriter writer, Bundle responsible){
		m_TypeToWriter.put(writer.getWriteClass(), writer);
		m_WriterToType.put(writer, writer.getWriteClass());
		notifyListeners(new CoreEvent(m_Core, Type.DATAWRITER_ADDED, responsible, writer.getWriteClass()));
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
	public synchronized void removeDataReader(Class<? extends Data> type, Bundle responsible){
		removeDataReader(type, m_TypeToReader.get(type), responsible);
	}
	
	@Override
	public synchronized void removeDataWriter(Class<? extends Data> type, Bundle responsible){
		removeDataWriter(type, m_TypeToWriter.get(type), responsible);
	}
	
	@Override
	public synchronized void removeDataReader(DataReader reader, Bundle responsible){
		removeDataReader(m_ReaderToType.get(reader), reader, responsible);
	}
	
	@Override
	public synchronized void removeDataWriter(DataWriter writer, Bundle responsible){
		removeDataWriter(m_WriterToType.get(writer), writer, responsible);
	}
	
	@Override
	public synchronized Collection<Class<? extends Data>> getReadableDataTypes(){
		return m_TypeToReader.keySet();
	}
	
	@Override
	public synchronized Collection<Class<? extends Data>> getWritableDataTypes(){
		return m_TypeToWriter.keySet();
	}
	
	public synchronized DataReader getReader(Class<? extends Data> type){
		return m_TypeToReader.get(type);
	}
	
	public synchronized DataWriter getWriter(Class<? extends Data> type){
		return m_TypeToWriter.get(type);
	}
	
	public synchronized Class<? extends Data> getReadType(DataReader reader){
		return m_ReaderToType.get(reader);
	}
	
	public synchronized Class<? extends Data> getWriteType(DataWriter writer){
		return m_WriterToType.get(writer);
	}

	@Override
	public synchronized FileFilter[] getValidReadFormats(Class<? extends Data> type) {
		DataReader reader = m_TypeToReader.get(type);
		if(reader == null){
			return new FileFilter[]{};
		}
		return reader.getValidReadFormats();
	}

	@Override
	public synchronized FileNameExtensionFilter[] getValidWriteFormats(Class<? extends Data> type) {
		DataWriter writer = m_TypeToWriter.get(type);
		if(writer == null){
			return new FileNameExtensionFilter[]{};
		}
		return writer.getValidWriteFormats();
	}

	@Override
	public synchronized Data readData(File file, Class<? extends Data> type, FileFilter format, Bundle responsible) throws Exception {
		if(!this.isReadable(type)){
			throw new Exception(type+" not readable");
		}
		DataReader reader = m_TypeToReader.get(type);
		if(reader == null){
			throw new Exception(type+" reader is null");
		}
		try{
			FileInputStream in = new FileInputStream(file);
			Data read = reader.read(format, in);
			this.addData(read, responsible);
			in.close();
			return read;
		} catch(Exception e){
			throw e;
		}
	}

	@Override
	public synchronized void writeData(File file, Data data, FileNameExtensionFilter format) throws Exception {
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
	public synchronized boolean contains(String name) {
		return m_NameToData.containsKey(name);
	}

	@Override
	public synchronized boolean contains(Data data) {
		return m_DataToName.containsKey(data);
	}
	
	public synchronized void clearData(){
		m_Data.clear();
		m_NameToData.clear();
		m_DataToName.clear();
		m_Counts.clear();
		this.notifyListeners(new CoreEvent(m_Core, Type.WORKSPACE_CLEARED, JProbeActivator.getBundle()));
		this.m_ChangesSinceLastSave = false;
	}
	
	@Override
	public synchronized boolean changedSinceSave(){
		return m_ChangesSinceLastSave;
	}

	@Override
	public synchronized long save(OutputStream out) {
		ByteCounterOutputStream counter = new ByteCounterOutputStream(out);
		try {
			ObjectOutputStream oout = new ObjectOutputStream(counter);
			for(Data stored : m_DataToName.keySet()){
				String name = this.getDataName(stored);
				String bundle = m_DataProviders.get(stored.getClass());
				oout.writeObject(name);
				oout.writeObject(bundle);
				oout.writeObject(stored);
			}
			m_ChangesSinceLastSave = false;
			oout.close();
		} catch (IOException e) {
			ErrorHandler.getInstance().handleException(e, JProbeActivator.getBundle());	
		}
		return counter.bytesWritten();
	}

	@Override
	public synchronized void load(InputStream in) {
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
					this.addData(data, name, JProbeActivator.getBundle(), false);
				} catch (ClassNotFoundException e) {
					//do nothing, this means the plugin that provides the data type is not loaded so simply proceed
					continue;
				} catch (Exception e){
					finished = true;
				}
			}
			m_ChangesSinceLastSave = false;
			this.notifyListeners(new CoreEvent(m_Core, Type.WORKSPACE_LOADED, JProbeActivator.getBundle()));
			oin.close();
		} catch (IOException e) {
			ErrorHandler.getInstance().handleException(e, JProbeActivator.getBundle());
		}
	}

	public void load(){
		m_ReaderListener.load();
		m_WriterListener.load();
	}
	
	public void unload(){
		m_ReaderListener.unload();
		m_WriterListener.unload();
	}
	
	
	
	
	
	
}
