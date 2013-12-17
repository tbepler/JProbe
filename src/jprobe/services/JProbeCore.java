package jprobe.services;

import java.awt.GridBagConstraints;
import java.io.File;
import java.util.Collection;

import javax.swing.JComponent;
import javax.swing.JMenu;

public interface JProbeCore {
	
	public void addComponent(JComponent component, GridBagConstraints constrains);
	public void removeComponent(JComponent component);
	
	public void addDropdownMenu(JMenu menu);
	public void removeDropdownMenu(JMenu menu);
	
	public void addCoreListener(CoreListener listener);
	public void removeCoreListener(CoreListener listener);
	
	public void addSaveable(Saveable add);
	public void removeSaveable(Saveable remove);
	
	public void addFunction(Function f);
	public void removeFunction(Function f);
	public Function[] getAllFunctions();
	
	public void addDataReader(Class<? extends Data> read, DataReader reader);
	public void removeDataReader(DataReader reader);
	public Collection<Class<? extends Data>> getReadableDataClasses();
	public String[] getValidReadFormats(Class<? extends Data> dataClass);
	
	public void addDataWriter(Class<? extends Data> write, DataWriter writer);
	public void removeDataWriter(DataWriter writer);
	public Collection<Class<? extends Data>> getWritableDataClasses();
	public String[] getValidWriteFormats(Class<? extends Data> dataClass);
	
	public void addData(Data data);
	public void removeData(Data data);
	public Data readData(File file, Class<? extends Data> type, String format) throws Exception;
	public void writeData(File file, Data data, String format) throws Exception;
	public Data[] getData();
	public String[] getDataNames();
	public void rename(Data data, String name);
	public String getName(Data data);
	public Data getData(String name);
	public Data[] getData(Class<? extends Data> type);
	
}
