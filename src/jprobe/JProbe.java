package jprobe;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import jprobe.save.LoadException;
import jprobe.save.SaveException;
import jprobe.save.SaveManager;
import jprobe.services.CoreListener;
import jprobe.services.DataManager;
import jprobe.services.Debug;
import jprobe.services.ErrorHandler;
import jprobe.services.FunctionManager;
import jprobe.services.JProbeCore;
import jprobe.services.Log;
import jprobe.services.Saveable;

import org.apache.felix.framework.Felix;
import org.apache.felix.framework.util.FelixConstants;
import org.apache.felix.main.AutoProcessor;
import org.apache.felix.main.Main;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.Constants;

public class JProbe implements JProbeCore{
	
	private Mode m_Mode;
	private CommandManager m_CmdManager;
	private CoreDataManager m_DataManager;
	private CoreFunctionManager m_FunctionManager;
	private SaveManager m_SaveManager;
	private JProbeActivator m_Activator;
	private Felix m_Felix;
	
	public JProbe(Configuration config){
		m_Mode = config.getDefaultMode();
		String[] args = config.getArgs();
		if(args.length > 0){
			if(args[0].equals(jprobe.Constants.ARG_INTERACTIVE_MODE)){
				m_Mode = Mode.INTERACTIVE;
			}else if(args[0].equals(jprobe.Constants.ARG_COMMAND_MODE)){
				m_Mode = Mode.COMMAND;
				String[] newArgs = new String[args.length-1];
				System.arraycopy(args, 1, newArgs, 0, newArgs.length);
				args = newArgs;
			}else{
				m_Mode = Mode.COMMAND;
			}
		}
		m_DataManager = new CoreDataManager(this, null);
		m_FunctionManager = new CoreFunctionManager(this);
		m_SaveManager = new SaveManager();
		m_SaveManager.addSaveable(m_DataManager, "core");
		//create felix config map
		Map felixConfig = new HashMap();
		//export the core service package
		felixConfig.put(Constants.FRAMEWORK_SYSTEMPACKAGES_EXTRA, "jprobe.services;version=1.0.0," +
				"jprobe.services.data;version=1.0.0," +
				"jprobe.services.function;version=1.0.0,"
				+ "jprobe.services.command;version=1.0.0,"
				+ "util.progress;version=1.0.0,"
				+ "util.gui;version=1.0.0,"
				+ "util;version=1.0.0,"
				+ "util.genome;version=1.0.0,"
				+ "util.genome.reader;version=1.0.0,"
				+ "util.genome.reader.query;version=1.0.0,"
				+ "util.genome.reader.threaded;version=1.0.0,"
				+ "util.genome.peak;version=1.0.0,"
				+ "util.xmlserializer;version=1.0.0");
		felixConfig.put(Constants.FRAMEWORK_BOOTDELEGATION, "javax.swing,"
				+ "javax.swing.*");
		felixConfig.put(FelixConstants.FRAMEWORK_STORAGE_CLEAN, config.getFelixStorageClean());
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
			props.setProperty(AutoProcessor.AUTO_DEPLOY_DIR_PROPERY, config.getAutoDeployPluginDirectory());
			props.setProperty(AutoProcessor.AUTO_DEPLOY_ACTION_PROPERY, "install,start");
			m_Felix.init();
			AutoProcessor.process(props, m_Felix.getBundleContext());
			//start the felix instance
			m_Felix.start();
		} catch (Exception e){
			System.err.println("Error creating Felix framework: "+e);
			e.printStackTrace();
		}
		m_DataManager.setBundleContext(m_Activator.getBundleContext());
		m_CmdManager = m_Activator.getCommandManager();
		if(m_Mode == Mode.COMMAND){
			m_CmdManager.execute(this, args);
			this.shutdown();;
		}
	}
	
	@Override
	public void shutdown(){
		try{
			if(Debug.getLevel() == Debug.FULL || Debug.getLevel() == Debug.LOG){
				Log.getInstance().write(JProbeActivator.getBundle(), "JProbe shutting down.");
			}
			m_Felix.stop();
			//System.out.println("Waiting for stop");
			//m_Felix.waitForStop(0);
			System.exit(0);
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
		try {
			m_SaveManager.save(toFile);
		} catch (SaveException e) {
			ErrorHandler.getInstance().handleException(e, m_Activator.getBundleContext().getBundle());
		}
	}
	
	@Override
	public void load(File fromFile){
		try {
			m_SaveManager.load(fromFile);
		} catch (LoadException e) {
			ErrorHandler.getInstance().handleException(e, m_Activator.getBundleContext().getBundle());
		}
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


	
}
