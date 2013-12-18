package jprobe;

import java.awt.GridBagConstraints;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;

import javax.swing.JComponent;
import javax.swing.JMenu;

import jprobe.services.CoreListener;
import jprobe.services.Data;
import jprobe.services.DataReader;
import jprobe.services.DataWriter;
import jprobe.services.Function;
import jprobe.services.JProbeCore;
import jprobe.services.Saveable;

import org.apache.felix.framework.Felix;
import org.apache.felix.framework.util.FelixConstants;
import org.apache.felix.main.AutoProcessor;
import org.apache.felix.main.Main;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.Constants;

public class JProbe implements JProbeCore{
	
	private JProbeGUIFrame frame;
	private DataManager dataManager;
	private FunctionManager functionManager;
	private JProbeActivator activator;
	private Felix felix;
	
	public JProbe(){
		dataManager = new DataManager(this);
		functionManager = new FunctionManager(this);
		frame = new JProbeGUIFrame(this, "JProbe");
		//create felix config map
		Map config = new HashMap();
		//export the core service package
		config.put(Constants.FRAMEWORK_SYSTEMPACKAGES_EXTRA, "jprobe.services; version=1.0.0");
		config.put(Constants.FRAMEWORK_BOOTDELEGATION, "javax.swing");
		config.put(FelixConstants.FRAMEWORK_STORAGE_CLEAN, "onFirstInit");
		//create activator and add to config map
		activator = new JProbeActivator(this);
		List<BundleActivator> l = new ArrayList<BundleActivator>();
		l.add(activator);
		config.put(FelixConstants.SYSTEMBUNDLE_ACTIVATORS_PROP, l);
		
		try{
			//create and instance of the felix framework using the config map
			felix = new Felix(config);
			Properties props = new Properties();
			Main.copySystemProperties(props);
			props.setProperty(AutoProcessor.AUTO_DEPLOY_DIR_PROPERY, "plugins");
			props.setProperty(AutoProcessor.AUTO_DEPLOY_ACTION_PROPERY, "install,start");
			felix.init();
			AutoProcessor.process(props, felix.getBundleContext());
			//start the felix instance
			felix.start();
		} catch (Exception e){
			System.err.println("Error creating Felix framework: "+e);
			e.printStackTrace();
		}
		frame.pack();
		frame.setVisible(true);
	}
	
	void shutdown(){
		try{
			felix.stop();
			felix.waitForStop(0);
		} catch (Exception e){
			System.err.println("Error shutting down Felix framework: "+e);
			e.printStackTrace();
		}
	}

	@Override
	public void addComponent(JComponent component, GridBagConstraints constrains) {
		frame.addComponent(component, constrains);
	}

	@Override
	public void removeComponent(JComponent component) {
		frame.removeComponent(component);
	}

	@Override
	public void addDropdownMenu(JMenu menu) {
		frame.addDropdownMenu(menu);
	}

	@Override
	public void removeDropdownMenu(JMenu menu) {
		frame.removeDropdownMenu(menu);
	}

	@Override
	public void addCoreListener(CoreListener listener) {
		// TODO Auto-generated method stub
		dataManager.addListener(listener);
		functionManager.addListener(listener);
	}

	@Override
	public void removeCoreListener(CoreListener listener) {
		// TODO Auto-generated method stub
		dataManager.removeListener(listener);
		functionManager.removeListener(listener);
	}

	@Override
	public void addFunction(Function f) {
		functionManager.addFunction(f);
	}

	@Override
	public void removeFunction(Function f) {
		functionManager.removeFunction(f);
	}

	@Override
	public Function[] getAllFunctions() {
		return functionManager.getAllFunctions();
	}
	
	@Override
	public Function[] getFunctions(String name){
		return functionManager.getFunctions(name);
	}
	
	@Override
	public String[] getFunctionNames(){
		return functionManager.getFunctionNames();
	}

	@Override
	public void addDataReader(Class<? extends Data> read, DataReader reader) {
		dataManager.addDataReader(read, reader);
	}

	@Override
	public void removeDataReader(DataReader reader) {
		dataManager.removeDataReader(reader);
	}

	@Override
	public Collection<Class<? extends Data>> getReadableDataClasses() {
		return dataManager.getReadableDataTypes();
	}

	@Override
	public String[] getValidReadFormats(Class<? extends Data> dataClass) {
		DataReader reader = dataManager.getReader(dataClass);
		Map<String, String[]> formats = reader.getValidReadFormats();
		return formats.keySet().toArray(new String[formats.size()]);
	}

	@Override
	public void addDataWriter(Class<? extends Data> write, DataWriter writer) {
		dataManager.addDataWriter(write, writer);
	}

	@Override
	public void removeDataWriter(DataWriter writer) {
		dataManager.removeDataWriter(writer);
	}

	@Override
	public Collection<Class<? extends Data>> getWritableDataClasses() {
		return dataManager.getWritableDataTypes();
	}

	@Override
	public String[] getValidWriteFormats(Class<? extends Data> dataClass) {
		DataWriter writer = dataManager.getWriter(dataClass);
		Map<String, String[]> formats = writer.getValidWriteFormats();
		return formats.keySet().toArray(new String[formats.size()]);
	}

	@Override
	public void addData(Data data) {
		dataManager.addData(data);
	}

	@Override
	public void removeData(Data data) {
		dataManager.removeData(data);
	}

	@Override
	public Data readData(File file, Class<? extends Data> type, String format) throws Exception {
		if(!dataManager.isReadable(type)){
			throw new Exception("Error: "+type+" not readable");
		}
		DataReader reader = dataManager.getReader(type);
		try{
			Data read = reader.read(format, new Scanner(file));
			dataManager.addData(read);
			return read;
		} catch(Exception e){
			throw e;
		}
	}

	@Override
	public void writeData(File file, Data data, String format) throws Exception {
		if(!dataManager.isWritable(data.getClass())){
			throw new Exception("Error: "+data.getClass()+" not writable");
		}
		DataWriter writer = dataManager.getWriter(data.getClass());
		try{
			writer.write(data, format, new BufferedWriter(new FileWriter(file)));
		} catch(Exception e){
			throw e;
		}
	}

	@Override
	public Data[] getData() {
		List<Data> data = dataManager.getAllData();
		return data.toArray(new Data[data.size()]);
	}
	
	@Override
	public String[] getDataNames() {
		return dataManager.getDataNames();
	}

	@Override
	public void rename(Data data, String name) {
		dataManager.rename(data, name);
	}

	@Override
	public String getName(Data data) {
		return dataManager.getDataName(data);
	}

	@Override
	public Data getData(String name) {
		return dataManager.getData(name);
	}
	
	@Override
	public Data[] getData(Class<? extends Data> type){
		List<Data> data = dataManager.getData(type);
		return data.toArray(new Data[data.size()]);
	}

	@Override
	public void addSaveable(Saveable add) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeSaveable(Saveable remove) {
		// TODO Auto-generated method stub
		
	}


	
}
