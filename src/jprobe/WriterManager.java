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
import jprobe.services.data.DataWriter;

public class WriterManager extends AbstractServiceListener<DataWriter>{

	private final Collection<CoreListener> m_Listeners = new HashSet<CoreListener>();
	
	private final Collection<DataWriter<?>> m_Writer = new HashSet<DataWriter<?>>();
	private final Map<Class<? extends Data>, Collection<DataWriter<?>>> m_ClassWriters = new HashMap<Class<? extends Data>, Collection<DataWriter<?>>>();
	
	private final JProbeCore m_Core;
	
	public WriterManager(JProbeCore core) {
		super(DataWriter.class);
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
	
	public Collection<DataWriter<? extends Data>> getDataWriters() {
		return Collections.unmodifiableCollection(m_Writer);
	}

	public Collection<DataWriter<? extends Data>> getDataWriters(Class<? extends Data> writeClass) {
		if(m_ClassWriters.containsKey(writeClass)){
			return Collections.unmodifiableCollection(m_ClassWriters.get(writeClass));
		}
		Collection<DataWriter<?>> empty = new HashSet<DataWriter<?>>();
		m_ClassWriters.put(writeClass, empty);
		return Collections.unmodifiableCollection(empty);
	}

	public boolean isWritable(Class<? extends Data> dataClass) {
		return m_ClassWriters.containsKey(dataClass) && !m_ClassWriters.get(dataClass).isEmpty();
	}
	
	protected void addDataWriter(DataWriter writer){
		m_Writer.add(writer);
		Class<? extends Data> clazz = writer.getWriteClass();
		if(m_ClassWriters.containsKey(clazz)){
			m_ClassWriters.get(clazz).add(writer);
		}else{
			Collection<DataWriter> classReaders = new HashSet<DataWriter>();
			classReaders.add(writer);
			m_ClassWriters.put(clazz, classReaders);
		}
		this.notifyListeners(new CoreEvent(Type.DATAWRITER_ADDED, writer));
	}
	
	protected void removeDataWriter(DataWriter writer){
		if(m_Writer.contains(writer)){
			m_Writer.remove(writer);
		}
		Class<? extends Data> clazz = writer.getWriteClass();
		if(m_ClassWriters.containsKey(clazz)){
			m_ClassWriters.get(clazz).remove(writer);
		}
		this.notifyListeners(new CoreEvent(Type.DATAWRITER_REMOVED, writer));
	}

	@Override
	public void register(DataWriter service, Bundle provider) {
		this.addDataWriter(service);
		if(Debug.getLevel() == Debug.FULL || Debug.getLevel() == Debug.LOG){
			JProbeLog.getInstance().write(JProbeActivator.getBundle(), "DataWriter for class "+service.getWriteClass()+" added by bundle: "+provider.getSymbolicName());
		}
	}

	@Override
	public void unregister(DataWriter service, Bundle provider) {
		this.removeDataWriter(service);
		if(Debug.getLevel() == Debug.FULL || Debug.getLevel() == Debug.LOG){
			JProbeLog.getInstance().write(JProbeActivator.getBundle(), "DataWriter for class "+service.getWriteClass()+" removed by bundle: "+provider.getSymbolicName());
		}
	}

}
