package jprobe.system.model;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.swing.filechooser.FileNameExtensionFilter;

import jprobe.services.CoreEvent;
import jprobe.services.CoreEvent.Type;
import jprobe.services.CoreListener;
import jprobe.services.JProbeCore;
import jprobe.services.data.Data;
import jprobe.services.data.DataWriter;
import jprobe.services.data.WriteException;

public class WriterManager{

	private final Collection<CoreListener> m_Listeners = new HashSet<CoreListener>();
	
	private final Collection<DataWriter<?>> m_Writer = new HashSet<DataWriter<?>>();
	private final Map<Class<? extends Data>, Collection<DataWriter<?>>> m_ClassWriters = new HashMap<Class<? extends Data>, Collection<DataWriter<?>>>();
	
	private final JProbeCore m_Core;
	
	public WriterManager(JProbeCore core) {
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
	
	public Collection<Class<? extends Data>> getWritableDataClasses(){
		return Collections.unmodifiableCollection(m_ClassWriters.keySet());
	}
	
	public List<FileNameExtensionFilter> getWriteFormats(Class<? extends Data> writeClass){
		List<FileNameExtensionFilter> formats = new ArrayList<FileNameExtensionFilter>();
		if(m_ClassWriters.containsKey(writeClass)){
			for(DataWriter<?> writer : m_ClassWriters.get(writeClass)){
				formats.addAll(writer.getWriteFormats());
			}
		}
		return formats;
	}
	
	public void writeData(Data d, FileNameExtensionFilter format, OutputStream out) throws WriteException{
		Collection<DataWriter<?>> writers = this.getDataWriters(d.getClass());
		if(!writers.isEmpty()){
			for(DataWriter<?> w : writers){
				if(w.getWriteFormats().contains(format)){
					this.writeData(w, d, format, out);
					return;
				}
			}
			throw new WriteException("Unable to write data class: "+d.getClass()+" with format: "+format.getDescription());
		}
		throw new WriteException("Unable to write data class: "+d.getClass());
	}
	
	private <D extends Data> void writeData(DataWriter<D> writer, Data d, FileNameExtensionFilter format, OutputStream out) throws WriteException{
		D write = writer.getWriteClass().cast(d);
		writer.write(write, format, out);
	}
	
	public Collection<DataWriter<? extends Data>> getDataWriters() {
		return Collections.unmodifiableCollection(m_Writer);
	}

	public Collection<DataWriter<? extends Data>> getDataWriters(Class<? extends Data> writeClass) {
		Collection<DataWriter<?>> writers = new HashSet<DataWriter<?>>();
		for(Class<? extends Data> clazz : m_ClassWriters.keySet()){
			if(clazz.isAssignableFrom(writeClass)){
				writers.addAll(m_ClassWriters.get(clazz));
			}
		}
		return Collections.unmodifiableCollection(writers);
	}

	public boolean isWritable(Class<? extends Data> dataClass) {
		
		return m_ClassWriters.containsKey(dataClass) && !m_ClassWriters.get(dataClass).isEmpty();
	}
	
	/**
	 * Returns True if {@link DataWriter} was added, False if it was already contained.
	 * @param writer
	 * @return
	 */
	public boolean addDataWriter(DataWriter<?> writer){
		if(m_Writer.contains(writer)){
			return false;
		}
		m_Writer.add(writer);
		Class<? extends Data> clazz = writer.getWriteClass();
		if(m_ClassWriters.containsKey(clazz)){
			Collection<DataWriter<?>> col = m_ClassWriters.get(clazz);
			col.add(writer);
		}else{
			Collection<DataWriter<?>> classReaders = new HashSet<DataWriter<?>>();
			classReaders.add(writer);
			m_ClassWriters.put(clazz, classReaders);
			this.notifyListeners(new CoreEvent(Type.DATA_WRITABLE, clazz));
		}
		return true;
	}
	
	/**
	 * Returns True if specified {@link DataWriter} was removed, False otherwise.
	 * @param writer
	 * @return
	 */
	public boolean removeDataWriter(DataWriter<?> writer){
		boolean removed = false;
		if(m_Writer.contains(writer)){
			removed = m_Writer.remove(writer);
		}
		Class<? extends Data> clazz = writer.getWriteClass();
		if(m_ClassWriters.containsKey(clazz)){
			Collection<DataWriter<?>> col = m_ClassWriters.get(clazz);
			col.remove(writer);
			if(col.isEmpty()){
				m_ClassWriters.remove(clazz);
				this.notifyListeners(new CoreEvent(Type.DATA_UNWRITABLE, clazz));
			}
		}
		return removed;
	}

}
