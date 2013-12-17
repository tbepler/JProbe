package jprobe;

import java.awt.GridBagConstraints;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JMenu;

import jprobe.services.CoreListener;
import jprobe.services.Data;
import jprobe.services.DataReader;
import jprobe.services.DataWriter;
import jprobe.services.Function;
import jprobe.services.JProbeCore;

import org.apache.felix.framework.Felix;
import org.apache.felix.framework.util.FelixConstants;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.Constants;

public class JProbe implements JProbeCore{
	
	private JProbeGUIFrame frame;
	private JProbeActivator activator;
	private Felix felix;
	
	public JProbe(){
		
		JProbeGUIFrame frame = new JProbeGUIFrame(this, "JProbe");
		//create felix config map
		Map config = new HashMap();
		//export the core service package
		config.put(Constants.FRAMEWORK_SYSTEMPACKAGES_EXTRA, "jprobe.services; version=1.0.0");
		//create activator and add to config map
		activator = new JProbeActivator(this);
		List<BundleActivator> l = new ArrayList<BundleActivator>();
		l.add(activator);
		config.put(FelixConstants.SYSTEMBUNDLE_ACTIVATORS_PROP, l);
		
		try{
			//create and instance of the felix framework using the config map
			felix = new Felix(config);
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
		
	}

	@Override
	public void removeCoreListener(CoreListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addFunction(Function f) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeFunction(Function f) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Function[] getAllFunctions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addDataReader(Class<? extends Data> read, DataReader reader) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeDataReader(DataReader reader) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Class<? extends Data>[] getReadableDataClasses() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getValidReadFormats(Class<? extends Data> dataClass) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addDataWriter(Class<? extends Data> write, DataWriter writer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeDataWriter(DataWriter writer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Class<? extends Data>[] getWritableDataClasses() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getValidWriteFormats(Class<? extends Data> dataClass) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addData(Data data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeData(Data data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Data readData(File file, String format) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void writeData(File file, Data data, String format) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Data[] getData() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
