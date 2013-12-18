package jprobe.services;

import java.awt.GridBagConstraints;
import java.io.File;
import java.util.Collection;

import javax.swing.JComponent;
import javax.swing.JMenu;

import org.osgi.framework.Bundle;

public interface JProbeCore {
	
	public void addComponent(JComponent component, GridBagConstraints constrains, Bundle responsible);
	public void removeComponent(JComponent component, Bundle responsible);
	
	public void addDropdownMenu(JMenu menu, Bundle responsible);
	public void removeDropdownMenu(JMenu menu, Bundle responsible);
	
	public void addCoreListener(CoreListener listener);
	public void removeCoreListener(CoreListener listener);
	
	public void addSaveable(Saveable add);
	public void removeSaveable(Saveable remove);
	
	public void addFunction(Function f, Bundle responsible);
	public void removeFunction(Function f, Bundle responsible);
	public Function[] getAllFunctions();
	public Function[] getFunctions(String name);
	public String[] getFunctionNames();
	
	public void addDataReader(Class<? extends Data> read, DataReader reader, Bundle responsible);
	public void removeDataReader(DataReader reader, Bundle responsible);
	public Collection<Class<? extends Data>> getReadableDataClasses();
	public String[] getValidReadFormats(Class<? extends Data> dataClass);
	
	public void addDataWriter(Class<? extends Data> write, DataWriter writer, Bundle responsible);
	public void removeDataWriter(DataWriter writer, Bundle responsible);
	public Collection<Class<? extends Data>> getWritableDataClasses();
	public String[] getValidWriteFormats(Class<? extends Data> dataClass);
	
	public void addData(Data data, Bundle responsible);
	public void removeData(Data data, Bundle responsible);
	public Data readData(File file, Class<? extends Data> type, String format, Bundle responsible) throws Exception;
	public void writeData(File file, Data data, String format) throws Exception;
	public Data[] getData();
	public String[] getDataNames();
	public void rename(Data data, String name, Bundle responsible);
	public String getName(Data data);
	public Data getData(String name);
	public Data[] getData(Class<? extends Data> type);
	
}
