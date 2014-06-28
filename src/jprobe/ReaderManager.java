package jprobe;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.osgi.framework.Bundle;

import jprobe.services.AbstractServiceListener;
import jprobe.services.CoreEvent;
import jprobe.services.CoreEvent.Type;
import jprobe.services.CoreListener;
import jprobe.services.Debug;
import jprobe.services.JProbeCore;
import jprobe.services.JProbeLog;
import jprobe.services.data.Data;
import jprobe.services.data.DataReader;

public class ReaderManager extends AbstractServiceListener<DataReader>{

	private final Collection<CoreListener> m_Listeners = new HashSet<CoreListener>();
	
	private final Collection<DataReader> m_Readers = new HashSet<DataReader>();
	private final Map<Class<? extends Data>, Collection<DataReader>> m_ClassReaders = new HashMap<Class<? extends Data>, Collection<DataReader>>();
	
	private final JProbeCore m_Core;
	
	public ReaderManager(JProbeCore core) {
		super(DataReader.class);
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
	
	public Collection<DataReader> getDataReaders() {
		return Collections.unmodifiableCollection(m_Readers);
	}

	public Collection<DataReader> getDataReaders(Class<? extends Data> readClass) {
		if(m_ClassReaders.containsKey(readClass)){
			return Collections.unmodifiableCollection(m_ClassReaders.get(readClass));
		}
		Collection<DataReader> empty = new HashSet<DataReader>();
		m_ClassReaders.put(readClass, empty);
		return Collections.unmodifiableCollection(empty);
	}

	public boolean isReadable(Class<? extends Data> dataClass) {
		return m_ClassReaders.containsKey(dataClass) && !m_ClassReaders.get(dataClass).isEmpty();
	}
	
	protected void addDataReader(DataReader reader){
		m_Readers.add(reader);
		Class<? extends Data> clazz = reader.getReadClass();
		if(m_ClassReaders.containsKey(clazz)){
			m_ClassReaders.get(clazz).add(reader);
		}else{
			Collection<DataReader> classReaders = new HashSet<DataReader>();
			classReaders.add(reader);
			m_ClassReaders.put(clazz, classReaders);
		}
		this.notifyListeners(new CoreEvent(Type.DATAREADER_ADDED, reader));
	}
	
	protected void removeDataReader(DataReader reader){
		if(m_Readers.contains(reader)){
			m_Readers.remove(reader);
		}
		Class<? extends Data> clazz = reader.getReadClass();
		if(m_ClassReaders.containsKey(clazz)){
			m_ClassReaders.get(clazz).remove(reader);
		}
		this.notifyListeners(new CoreEvent(Type.DATAREADER_REMOVED, reader));
	}

	@Override
	public void register(DataReader service, Bundle provider) {
		this.addDataReader(service);
		if(Debug.getLevel() == Debug.FULL || Debug.getLevel() == Debug.LOG){
			JProbeLog.getInstance().write(JProbeActivator.getBundle(), "DataReader for class "+service.getReadClass()+" added by bundle: "+provider.getSymbolicName());
		}
	}

	@Override
	public void unregister(DataReader service, Bundle provider) {
		this.removeDataReader(service);
		if(Debug.getLevel() == Debug.FULL || Debug.getLevel() == Debug.LOG){
			JProbeLog.getInstance().write(JProbeActivator.getBundle(), "DataReader for class "+service.getReadClass()+" removed by bundle: "+provider.getSymbolicName());
		}
	}

}
