package jprobe;

import java.io.BufferedWriter;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import jprobe.save.SaveManager;
import jprobe.services.CoreListener;
import jprobe.services.Workspace;
import jprobe.services.Debug;
import jprobe.services.ErrorHandler;
import jprobe.services.JProbeCore;
import jprobe.services.JProbeLog;
import jprobe.services.data.Data;
import jprobe.services.data.DataReader;
import jprobe.services.data.DataWriter;
import jprobe.services.function.Function;

import org.apache.felix.framework.Felix;
import org.apache.felix.framework.util.FelixConstants;
import org.apache.felix.main.AutoProcessor;
import org.apache.felix.main.Main;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.Constants;

import util.save.Saveable;

public class JProbe implements JProbeCore{
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static Map createFelixConfig(Configuration config, BundleActivator systemActivator) {
		Map felixConfig = new HashMap();
		//export the core service package
		felixConfig.put(Constants.FRAMEWORK_SYSTEMPACKAGES_EXTRA, jprobe.Constants.FELIX_EXPORT_PACKAGES);
		felixConfig.put(Constants.FRAMEWORK_BOOTDELEGATION, "javax.swing,javax.swing.*");
		felixConfig.put(FelixConstants.FRAMEWORK_STORAGE_CLEAN, config.getFelixStorageClean());
		felixConfig.put(FelixConstants.FRAMEWORK_STORAGE, jprobe.Constants.FELIX_CACHE_DIR);
		
		List<BundleActivator> l = new ArrayList<BundleActivator>();
		l.add(systemActivator);
		felixConfig.put(FelixConstants.SYSTEMBUNDLE_ACTIVATORS_PROP, l);
		return felixConfig;
	}
	
	private static Properties initSystemProperties() {
		Properties props = new Properties();
		Main.copySystemProperties(props);
		props.setProperty(AutoProcessor.AUTO_DEPLOY_DIR_PROPERY, jprobe.Constants.PLUGIN_AUTODEPLOY);
		props.setProperty(AutoProcessor.AUTO_DEPLOY_ACTION_PROPERY, "install,start");
		//set properties for the FileInstall bundle
		System.setProperty(jprobe.Constants.FELIX_FILE_INSTALL_DIR_PROP, jprobe.Constants.FELIX_WATCH_DIRS);
		System.setProperty(jprobe.Constants.FELIX_FILE_INSTALL_INITIALDELAY_PROP, jprobe.Constants.FELIX_INITIALDELAY);
		return props;
	}
	
	
	private final Mode m_Mode;
	private final JProbeActivator m_Activator;
	private final Felix m_Felix;
	private final FunctionManager m_FunctionManager;
	private final ReaderManager m_ReaderManager;
	private final WriterManager m_WriterManager;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public JProbe(Configuration config){
		String[] args = config.getArgs();
		if(args.length > 0 && args[0].matches(jprobe.Constants.GUI_REGEX)){
			m_Mode = Mode.GUI;
		}else{
			m_Mode = Mode.COMMAND;
		}
		m_FunctionManager = new FunctionManager(this);
		m_ReaderManager = new ReaderManager(this);
		m_WriterManager = new WriterManager(this);
		//create system bundle activator
		m_Activator = new JProbeActivator(this, m_FunctionManager, m_ReaderManager, m_WriterManager);
		//create felix config map
		Map felixConfig = createFelixConfig(config, m_Activator);
		
		try{
			//create an instance of the felix framework using the config map
			m_Felix = new Felix(felixConfig);
			//get properties to pass the felix
			Properties props = initSystemProperties();
			m_Felix.init();
			AutoProcessor.process(props, m_Felix.getBundleContext());
			//start the felix instance
			m_Felix.start();
		} catch (Exception e){
			System.err.println("Error creating Felix framework: "+e);
			e.printStackTrace();
			System.exit(1);
			//this should never happen... but the compiler needs this to allow later
			//references to m_Felix without "Field may not have been initialized"
			throw new Error();
		}
		if(m_Mode == Mode.COMMAND){ //parse args, execute, and quit
			this.parseAndExecute(args);
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

	private void parseAndExecute(String[] args) {
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
				JProbeLog.getInstance().write(JProbeActivator.getBundle(), "JProbe shutting down.");
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
		JProbeLog.getInstance().write(JProbeActivator.getBundle(), "Opened new workspace");
	}


	@Override
	public String getUserDir() {
		return jprobe.Constants.USER_JPROBE_DIR;
	}

	@Override
	public Workspace getWorkspace(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int indexOf(Workspace w) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Workspace> getWorkspaces() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void closeWorkspace(int index) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void closeWorkspace(Workspace w) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int numWorkspaces() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Collection<Function<?>> getFunctions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<DataReader> getDataReaders() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<DataReader> getDataReaders(Class<? extends Data> readClass) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isReadable(Class<? extends Data> dataClass) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Collection<DataWriter> getDataWriters() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<DataWriter> getDataWriters(
			Class<? extends Data> writeClass) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isWritable(Class<? extends Data> dataClass) {
		// TODO Auto-generated method stub
		return false;
	}




	
}
