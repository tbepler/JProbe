package jprobe;

import java.io.BufferedWriter;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import jprobe.save.SaveManager;
import jprobe.services.CoreListener;
import jprobe.services.DataManager;
import jprobe.services.Debug;
import jprobe.services.ErrorHandler;
import jprobe.services.FunctionManager;
import jprobe.services.JProbeCore;
import jprobe.services.LoadListener;
import jprobe.services.Log;
import jprobe.services.SaveListener;
import jprobe.services.Saveable;
import jprobe.services.data.Data;
import jprobe.services.data.DataWriter;

import org.apache.felix.framework.Felix;
import org.apache.felix.framework.util.FelixConstants;
import org.apache.felix.main.AutoProcessor;
import org.apache.felix.main.Main;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.Constants;

public class JProbe implements JProbeCore{
	
	private Mode m_Mode;
	private CoreDataManager m_DataManager;
	private CoreFunctionManager m_FunctionManager;
	private SaveManager m_SaveManager;
	private JProbeActivator m_Activator;
	private Felix m_Felix;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public JProbe(Configuration config){
		m_Mode = Mode.COMMAND;
		String[] args = config.getArgs();
		if(args.length > 0 && args[0].matches(jprobe.Constants.GUI_REGEX)){
			m_Mode = Mode.GUI;
		}
		//init save manager and start the save thread
		m_SaveManager = new SaveManager();
		//create felix config map
		Map felixConfig = new HashMap();
		//export the core service package
		felixConfig.put(Constants.FRAMEWORK_SYSTEMPACKAGES_EXTRA, jprobe.Constants.FELIX_EXPORT_PACKAGES);
		felixConfig.put(Constants.FRAMEWORK_BOOTDELEGATION, "javax.swing,"
				+ "javax.swing.*");
		felixConfig.put(FelixConstants.FRAMEWORK_STORAGE_CLEAN, config.getFelixStorageClean());
		felixConfig.put(FelixConstants.FRAMEWORK_STORAGE, jprobe.Constants.FELIX_CACHE_DIR);
		//create activator and add to config map
		m_Activator = new JProbeActivator(this);
		List<BundleActivator> l = new ArrayList<BundleActivator>();
		l.add(m_Activator);
		felixConfig.put(FelixConstants.SYSTEMBUNDLE_ACTIVATORS_PROP, l);
		
		try{
			//create and instance of the felix framework using the config map
			m_Felix = new Felix(felixConfig);
			Properties props = new Properties();
			Main.copySystemProperties(props);
			props.setProperty(AutoProcessor.AUTO_DEPLOY_DIR_PROPERY, jprobe.Constants.PLUGIN_AUTODEPLOY);
			props.setProperty(AutoProcessor.AUTO_DEPLOY_ACTION_PROPERY, "install,start");
			//set properties for the FileInstall bundle
			System.setProperty(jprobe.Constants.FELIX_FILE_INSTALL_DIR_PROP, jprobe.Constants.FELIX_WATCH_DIRS);
			System.setProperty(jprobe.Constants.FELIX_FILE_INSTALL_INITIALDELAY_PROP, jprobe.Constants.FELIX_INITIALDELAY);
			m_Felix.init();
			AutoProcessor.process(props, m_Felix.getBundleContext());
			//start the felix instance
			m_Felix.start();
			
		} catch (Exception e){
			System.err.println("Error creating Felix framework: "+e);
			e.printStackTrace();
		}
		m_DataManager.setBundleContext(m_Activator.getBundleContext());
		if(m_Mode == Mode.COMMAND){ //parse args, execute, and quit
			Data d = ParsingEngine.parseAndExecute(System.err, m_FunctionManager, args);
			if(d != null){
				DataWriter writer = m_DataManager.getDataWriter(d.getClass());
				try {
					BufferedWriter out = new BufferedWriter(new PrintWriter(System.out));
					writer.write(d, writer.getValidWriteFormats()[0], out);
					out.close();
				} catch (Exception e) {
					ErrorHandler.getInstance().handleException(e, JProbeActivator.getBundle());
				}
			}
			this.shutdown();
		}
		
		//wait for felix to stop
		try {
			m_Felix.waitForStop(0);
		} catch (InterruptedException e) {
			ErrorHandler.getInstance().handleException(e, JProbeActivator.getBundle());
		}
		//now exit the program
		System.exit(0);
	}
	
	void setDataManager(CoreDataManager dataManager){
		m_SaveManager.removeSaveable(m_DataManager, "core");
		m_DataManager = dataManager;
		m_SaveManager.addSaveable(m_DataManager, "core");
	}
	
	void setFunctionManager(CoreFunctionManager fncManager){
		m_FunctionManager = fncManager;
	}
	
	@Override
	public String getPreferencesDir(){
		return jprobe.Constants.PREFERENCES_DIR;
	}
	
	@Override
	public String getLogsDir(){
		return jprobe.Constants.LOG_DIR;
	}
	
	@Override
	public void shutdown(){
		try{
			if(Debug.getLevel() == Debug.FULL || Debug.getLevel() == Debug.LOG){
				Log.getInstance().write(JProbeActivator.getBundle(), "JProbe shutting down.");
			}
			m_Felix.stop();
		} catch (Exception e){
			ErrorHandler.getInstance().handleException(e, JProbeActivator.getBundle());
			e.printStackTrace();
		}
	}

	@Override
	public void addCoreListener(CoreListener listener) {
		//frame.addListener(listener);
		m_DataManager.addListener(listener);
		m_FunctionManager.addListener(listener);
		
	}

	@Override
	public void removeCoreListener(CoreListener listener) {
		//frame.removeListener(listener);
		m_DataManager.removeListener(listener);
		m_FunctionManager.removeListener(listener);
	}



	@Override
	public void addSaveable(Saveable add, Bundle bundle) {
		m_SaveManager.addSaveable(add, bundle.getSymbolicName());
	}

	@Override
	public void removeSaveable(Saveable remove, Bundle bundle) {
		m_SaveManager.removeSaveable(remove, bundle.getSymbolicName());
	}
	
	@Override
	public void save(File toFile){
		m_SaveManager.save(toFile);
	}
	
	@Override
	public void load(File fromFile){
		m_SaveManager.load(fromFile);
	}

	@Override
	public DataManager getDataManager() {
		return m_DataManager;
	}

	@Override
	public FunctionManager getFunctionManager() {
		return m_FunctionManager;
	}

	@Override
	public Mode getMode() {
		return m_Mode;
	}

	@Override
	public String getName() {
		return jprobe.Constants.NAME;
	}

	@Override
	public String getVersion() {
		return jprobe.Constants.VERSION;
	}

	@Override
	public void newWorkspace() {
		m_DataManager.clearData();
		Log.getInstance().write(JProbeActivator.getBundle(), "Opened new workspace");
	}

	@Override
	public boolean changedSinceLastSave() {
		return m_SaveManager.changesSinceSave();
	}

	@Override
	public String getUserDir() {
		return jprobe.Constants.USER_JPROBE_DIR;
	}

	@Override
	public void registerSave(SaveListener l) {
		m_SaveManager.registerSave(l);
	}

	@Override
	public void unregisterSave(SaveListener l) {
		m_SaveManager.unregisterSave(l);
	}

	@Override
	public void registerLoad(LoadListener l) {
		m_SaveManager.registerLoad(l);
	}

	@Override
	public void unregisterLoad(LoadListener l) {
		m_SaveManager.unregisterLoad(l);
	}


	
}
