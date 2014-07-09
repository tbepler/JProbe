package jprobe.system.model.managers;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.swing.filechooser.FileFilter;

import jprobe.services.CoreEvent;
import jprobe.services.CoreEvent.Type;
import jprobe.services.CoreListener;
import jprobe.services.JProbeCore;
import jprobe.services.data.Data;
import jprobe.services.data.DataReader;
import jprobe.services.data.ReadException;

public class ReaderManager{

	private final Collection<CoreListener> m_Listeners = new HashSet<CoreListener>();
	
	private final Collection<DataReader<?>> m_Readers = new HashSet<DataReader<?>>();
	private final Map<Class<? extends Data>, Collection<DataReader<?>>> m_ClassReaders = new HashMap<Class<? extends Data>, Collection<DataReader<?>>>();
	
	private final JProbeCore m_Core;
	
	public ReaderManager(JProbeCore core) {
		m_Core = core;
	}
	
	public void addCoreListener(CoreListener l){
		m_Listeners.add(l);
	}
	
	public void removeCoreListener(CoreListener l){
		m_Listeners.remove(l);
	}
	
	protected void notifyListeners(CoreEvent event){
		for(CoreListener l : m_Listeners){
			l.update(m_Core, event);
		}
	}
	
	public Collection<Class<? extends Data>> getReadableDataClasses(){
		return Collections.unmodifiableCollection(m_ClassReaders.keySet());
	}
	
	public List<FileFilter> getReadableFormats(Class<? extends Data> readClass){
		List<FileFilter> formats = new ArrayList<FileFilter>();
		if(m_ClassReaders.containsKey(readClass)){
			for(DataReader<?> reader : m_ClassReaders.get(readClass)){
				formats.addAll(reader.getReadFormats());
			}
		}
		return formats;
	}
	
	public <D extends Data> D readData(Class<D> readClass, FileFilter format, InputStream in) throws ReadException{
		if(m_ClassReaders.containsKey(readClass)){
			for(DataReader<?> r : m_ClassReaders.get(readClass)){
				try{
					@SuppressWarnings("unchecked")
					DataReader<D> reader = (DataReader<D>) r;
					if(reader.getReadFormats().contains(format)){
						return reader.read(format, in);
					}
				}catch(Exception e){
					//do nothing, this should never happen
				}
			}
			throw new ReadException("Unable to read data class: "+readClass+" using format: "+format.getDescription());
		}
		throw new ReadException("Unable to read data class: "+readClass);
	}
	
	public Collection<DataReader<?>> getDataReaders() {
		return Collections.unmodifiableCollection(m_Readers);
	}

	public Collection<DataReader<?>> getDataReaders(Class<? extends Data> readClass) {
		if(m_ClassReaders.containsKey(readClass)){
			return Collections.unmodifiableCollection(m_ClassReaders.get(readClass));
		}
		Collection<DataReader<?>> empty = new HashSet<DataReader<?>>();
		return Collections.unmodifiableCollection(empty);
	}

	public boolean isReadable(Class<? extends Data> dataClass) {
		return m_ClassReaders.containsKey(dataClass) && !m_ClassReaders.get(dataClass).isEmpty();
	}
	
	/**
	 * Returns True if the reader was successfully added to this manager. In other words, returns True
	 * if this manager did not already contain the specified reader, False otherwise.
	 * @param reader
	 * @return
	 */
	public boolean addDataReader(DataReader<?> reader){
		if(m_Readers.contains(reader)){
			return false;
		}
		m_Readers.add(reader);
		Class<? extends Data> clazz = reader.getReadClass();
		if(m_ClassReaders.containsKey(clazz)){
			Collection<DataReader<?>> classReaders = m_ClassReaders.get(clazz);
			classReaders.add(reader);
		}else{
			Collection<DataReader<?>> classReaders = new HashSet<DataReader<?>>();
			classReaders.add(reader);
			m_ClassReaders.put(clazz, classReaders);
			this.notifyListeners(new CoreEvent(Type.DATA_READABLE, clazz));
		}
		return true;
	}
	
	/**
	 * Returns True if the specified {@link DataReader} was removed from this manager, false otherwise.
	 * @param reader
	 * @return
	 */
	public boolean removeDataReader(DataReader<?> reader){
		boolean removed = false;
		if(m_Readers.contains(reader)){
			removed = m_Readers.remove(reader);
		}
		Class<? extends Data> clazz = reader.getReadClass();
		if(m_ClassReaders.containsKey(clazz)){
			Collection<DataReader<?>> col = m_ClassReaders.get(clazz);
			col.remove(reader);
			if(col.isEmpty()){
				m_ClassReaders.remove(clazz);
				this.notifyListeners(new CoreEvent(Type.DATA_UNREADABLE, clazz));
			}
		}
		return removed;
	}

}
