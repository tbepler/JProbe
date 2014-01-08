package jprobe;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import jprobe.services.error.ErrorHandler;
import jprobe.log.Log;
import jprobe.log.LogImplem;
import jprobe.services.CoreListener;
import jprobe.services.DataManager;
import jprobe.services.FunctionManager;
import jprobe.services.JProbeCore;
import jprobe.services.Saveable;

import org.apache.felix.framework.Felix;
import org.apache.felix.framework.util.FelixConstants;
import org.apache.felix.main.AutoProcessor;
import org.apache.felix.main.Main;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.Constants;

public class JProbe implements JProbeCore{
	
	//private JProbeGUIFrame frame;
	private CoreDataManager dataManager;
	private CoreFunctionManager functionManager;
	private CoreErrorHandler errorHandler;
	private JProbeActivator activator;
	private Debug debugLevel;
	private Felix felix;
	private Log log;
	
	public JProbe(Configuration config){
		log = new LogImplem(new File(config.getLogFile()));
		dataManager = new CoreDataManager(this);
		functionManager = new CoreFunctionManager(this);
		debugLevel = config.getDebugLevel();
		//frame = new JProbeGUIFrame(this, "JProbe");
		//create felix config map
		Map felixConfig = new HashMap();
		//export the core service package
		felixConfig.put(Constants.FRAMEWORK_SYSTEMPACKAGES_EXTRA, "jprobe.services;version=1.0.0,jprobe.services.error;version=1.0.0");
		felixConfig.put(Constants.FRAMEWORK_BOOTDELEGATION, "javax.swing,javax.swing.*");
		felixConfig.put(FelixConstants.FRAMEWORK_STORAGE_CLEAN, config.getFelixStorageClean());
		//create activator and add to config map
		activator = new JProbeActivator(this);
		List<BundleActivator> l = new ArrayList<BundleActivator>();
		l.add(activator);
		felixConfig.put(FelixConstants.SYSTEMBUNDLE_ACTIVATORS_PROP, l);
		
		try{
			//create and instance of the felix framework using the config map
			felix = new Felix(felixConfig);
			Properties props = new Properties();
			Main.copySystemProperties(props);
			props.setProperty(AutoProcessor.AUTO_DEPLOY_DIR_PROPERY, config.getAutoDeployPluginDirectory());
			props.setProperty(AutoProcessor.AUTO_DEPLOY_ACTION_PROPERY, "install,start");
			felix.init();
			errorHandler = new CoreErrorHandler(felix.getBundleContext(), log);
			AutoProcessor.process(props, felix.getBundleContext());
			//start the felix instance
			felix.start();
		} catch (Exception e){
			System.err.println("Error creating Felix framework: "+e);
			e.printStackTrace();
		}
		//frame.pack();
		//frame.setVisible(true);
	}
	
	public Log getLog(){
		return log;
	}
	
	@Override
	public void shutdown(){
		try{
			System.out.println("Shutting down");
			felix.stop();
			//System.out.println("Waiting for stop");
			//felix.waitForStop(0);
			System.out.println("Felix stopped");
			System.exit(0);
		} catch (Exception e){
			System.err.println("Error shutting down Felix framework: "+e);
			e.printStackTrace();
		}
	}

	@Override
	public void addCoreListener(CoreListener listener) {
		//frame.addListener(listener);
		dataManager.addListener(listener);
		functionManager.addListener(listener);
		
	}

	@Override
	public void removeCoreListener(CoreListener listener) {
		//frame.removeListener(listener);
		dataManager.removeListener(listener);
		functionManager.removeListener(listener);
	}



	@Override
	public void addSaveable(Saveable add) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeSaveable(Saveable remove) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ErrorHandler getErrorHandler() {
		return errorHandler;
	}

	@Override
	public void addErrorHandler(ErrorHandler handler) {
		errorHandler.addErrorHandler(handler);
	}

	@Override
	public void removeErrorHandler(ErrorHandler handler) {
		errorHandler.removeErrorHandler(handler);
	}

	@Override
	public DataManager getDataManager() {
		return dataManager;
	}

	@Override
	public FunctionManager getFunctionManager() {
		return functionManager;
	}


	
}
