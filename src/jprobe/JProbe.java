package jprobe;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.PrintWriter;
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
import jprobe.services.data.Data;
import jprobe.services.data.DataWriter;

import org.apache.felix.framework.Felix;
import org.apache.felix.framework.util.FelixConstants;
import org.apache.felix.main.AutoProcessor;
import org.apache.felix.main.Main;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.Constants;

import util.FileUtil;

public class JProbe implements JProbeCore{
	
	private Mode m_Mode;
	private CoreDataManager m_DataManager;
	private CoreFunctionManager m_FunctionManager;
	private SaveManager m_SaveManager;
	private JProbeActivator m_Activator;
	private Felix m_Felix;
	private AutosaveThread m_Autosave = null;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public JProbe(Configuration config){
		m_Mode = Mode.COMMAND;
		String[] args = config.getArgs();
		if(args.length > 0 && args[0].matches(jprobe.Constants.GUI_REGEX)){
			m_Mode = Mode.INTERACTIVE;
		}
		m_SaveManager = new SaveManager();
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
				+ "util.genome.kmer;version=1.0.0,"
				+ "util.genome.probe;version=1.0.0,"
				+ "util.genome.pwm;version=1.0.0,"
				+ "util.xmlserializer;version=1.0.0");
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
			this.shutdown();;
		}else{ //in gui mode, so check the config for workspace and autosave settings
			if(config.loadPrevWorkspace()){ //load the most recent saved workspace
				File newest = FileUtil.getMostRecentFile(jprobe.Constants.AUTOSAVE_DIR, new FileFilter(){
					@Override
					public boolean accept(File pathname) {
						return pathname.getName().endsWith(jprobe.Constants.WORKSPACE_FILE_EXTENSION);
					}
				});
				if(newest != null){
					this.load(newest);
				}
			}
			if(config.autosave()){ //init and start the autosave thread
				m_Autosave = new AutosaveThread(this, jprobe.Constants.AUTOSAVE_DIR, config.getAutosaveFrequency(), config.getMaxAutosaves());
				m_Autosave.start();
			}
		}
		
		try {
			m_Felix.waitForStop(0);
		} catch (InterruptedException e) {
			ErrorHandler.getInstance().handleException(e, JProbeActivator.getBundle());
		}
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
			if(m_Autosave != null){
				m_Autosave.terminate();
			}
			File session = new File(jprobe.Constants.AUTOSAVE_DIR + File.separator + "session." + jprobe.Constants.WORKSPACE_FILE_EXTENSION);
			this.save(session);
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
