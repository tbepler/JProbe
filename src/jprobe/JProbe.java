package jprobe;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import jprobe.services.CoreListener;
import jprobe.services.Workspace;
import jprobe.services.JProbeCore;
import jprobe.services.data.Data;
import jprobe.services.data.DataWriter;
import jprobe.services.data.ReadException;
import jprobe.services.data.WriteException;
import jprobe.services.function.Function;

import org.apache.felix.framework.Felix;
import org.apache.felix.framework.util.FelixConstants;
import org.apache.felix.main.AutoProcessor;
import org.apache.felix.main.Main;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.Constants;

import util.save.LoadException;

public class JProbe implements JProbeCore{
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static Map createFelixConfig(File felixCache, BundleActivator systemActivator) {
		Map felixConfig = new HashMap();
		//export the core service package
		felixConfig.put(Constants.FRAMEWORK_SYSTEMPACKAGES_EXTRA, jprobe.Constants.FELIX_EXPORT_PACKAGES);
		felixConfig.put(Constants.FRAMEWORK_BOOTDELEGATION, jprobe.Constants.FELIX_BOOTDELEGATION_PACKAGES);
		//no need to set storage clean, cache is deleted when program quits anyway
		//felixConfig.put(FelixConstants.FRAMEWORK_STORAGE_CLEAN, felixStorageClean);
		felixConfig.put(FelixConstants.FRAMEWORK_STORAGE, felixCache.getAbsoluteFile());
		
		List<BundleActivator> l = new ArrayList<BundleActivator>();
		l.add(systemActivator);
		felixConfig.put(FelixConstants.SYSTEMBUNDLE_ACTIVATORS_PROP, l);
		return felixConfig;
	}
	
	private static Properties initSystemProperties(File userPluginDir) {
		Properties props = new Properties();
		Main.copySystemProperties(props);
		props.setProperty(AutoProcessor.AUTO_DEPLOY_DIR_PROPERY, jprobe.Constants.PLUGIN_AUTODEPLOY);
		props.setProperty(AutoProcessor.AUTO_DEPLOY_ACTION_PROPERY, jprobe.Constants.FELIX_AUTODEPLOY_ACTION);
		//set properties for the FileInstall bundle
		System.setProperty(jprobe.Constants.FELIX_FILE_INSTALL_DIR_PROP, userPluginDir.getAbsolutePath());
		System.setProperty(jprobe.Constants.FELIX_FILE_INSTALL_INITIALDELAY_PROP, jprobe.Constants.FELIX_INITIALDELAY);
		return props;
	}
	
	private final JProbeActivator m_Activator;
	private final FunctionManager m_FunctionManager;
	private final ReaderManager m_ReaderManager;
	private final WriterManager m_WriterManager;
	private final WorkspaceManager m_WorkspaceManager;
	
	private final String m_UserDir;
	private final String m_LogsDir;
	private final String m_PropertiesDir;
	
	private Felix m_Felix = null;
	private Mode m_Mode = Mode.COMMAND;
	
	@SuppressWarnings({ "rawtypes" })
	public JProbe(String userDir, String logsDir, String propsDir){
		
		m_UserDir = userDir;
		m_LogsDir = logsDir;
		m_PropertiesDir = propsDir;
		
		m_FunctionManager = new FunctionManager(this);
		m_ReaderManager = new ReaderManager(this);
		m_WriterManager = new WriterManager(this);
		m_WorkspaceManager = new WorkspaceManager(this);
		//create system bundle activator
		m_Activator = new JProbeActivator(this, m_FunctionManager, m_ReaderManager, m_WriterManager, m_WorkspaceManager);

	}
	
	public void start(File userPluginsDir, File cacheDir, String[] args){
		//check to make sure this hasn't already been started
		if(m_Felix != null){
			return;
		}
		//set the mode to GUI according to the passed arguments, command is default
		if(args.length > 0 && args[0].matches(jprobe.Constants.GUI_REGEX)){
			m_Mode = Mode.GUI;
		}
		//create felix config map
		Map felixConfig = createFelixConfig(cacheDir, m_Activator);
		try{
			//create an instance of the felix framework using the config map
			m_Felix = new Felix(felixConfig);
			//get properties to pass the felix
			Properties props = initSystemProperties(userPluginsDir);
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
	}
	
	public void waitForShutdown() throws InterruptedException{
		m_Felix.waitForStop(0);
	}

	private void parseAndExecute(String[] args) {
		Data d = ParsingEngine.parseAndExecute(System.err, m_FunctionManager, args);
		if(d != null){
			Collection<DataWriter<?>> writers = m_WriterManager.getDataWriters(d.getClass());
			boolean writeSuccesful = false;
			for(DataWriter<?> writer : writers){
				try {
					OutputStream out = new BufferedOutputStream(System.out);
					this.writeData(writer, d, writer.getWriteFormats().get(0), out);
					out.close();
					writeSuccesful = true;
					break;
				} catch (Exception e) {
					//try next one
				}
			}
			if(!writeSuccesful){
				ErrorHandler.getInstance().handleException(new RuntimeException("Unable to write "+d.getClass()+"."), JProbeActivator.getBundle());
			}
		}
	}
	
	private <D extends Data> void writeData(DataWriter<D> writer, Data d, FileNameExtensionFilter format, OutputStream out) throws WriteException{
		D write = writer.getWriteClass().cast(d);
		writer.write(write, format, out);
	}
	
	@Override
	public String getUserDir() {
		return m_UserDir;
	}
	
	@Override
	public String getPreferencesDir(){
		return m_PropertiesDir;
	}
	
	@Override
	public String getLogsDir(){
		return m_LogsDir;
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
		m_FunctionManager.addListener(listener);
		m_ReaderManager.addCoreListener(listener);
		m_WriterManager.addCoreListener(listener);
		m_WorkspaceManager.addCoreListener(listener);
	}

	@Override
	public void removeCoreListener(CoreListener listener) {
		m_FunctionManager.removeListener(listener);
		m_ReaderManager.removeCoreListener(listener);
		m_WriterManager.removeCoreListener(listener);
		m_WorkspaceManager.removeCoreListener(listener);
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
	public Workspace newWorkspace() {
		return m_WorkspaceManager.newWorkspace();
	}
	
	@Override
	public Workspace openWorkspace(InputStream in, String source) throws LoadException{
		return m_WorkspaceManager.openWorkspace(in, source);
	}

	@Override
	public Workspace getWorkspace(int index) {
		return m_WorkspaceManager.getWorkspace(index);
	}

	@Override
	public int indexOf(Workspace w) {
		return m_WorkspaceManager.indexOf(w);
	}

	@Override
	public List<Workspace> getWorkspaces() {
		return m_WorkspaceManager.getWorkspaces();
	}

	@Override
	public void closeWorkspace(int index) {
		m_WorkspaceManager.closeWorkspace(index);
	}

	@Override
	public void closeWorkspace(Workspace w) {
		m_WorkspaceManager.closeWorkspace(w);
	}

	@Override
	public int numWorkspaces() {
		return m_WorkspaceManager.numWorkspaces();
	}

	@Override
	public Collection<Function<?>> getFunctions() {
		return m_FunctionManager.getFunctions();
	}

	@Override
	public boolean isReadable(Class<? extends Data> dataClass) {
		return m_ReaderManager.isReadable(dataClass);
	}


	@Override
	public boolean isWritable(Class<? extends Data> dataClass) {
		return m_WriterManager.isWritable(dataClass);
	}

	@Override
	public Collection<Class<? extends Data>> getReadableDataClasses() {
		return m_ReaderManager.getReadableDataClasses();
	}

	@Override
	public List<FileFilter> getReadFormats(Class<? extends Data> readClass) {
		return m_ReaderManager.getReadableFormats(readClass);
	}

	@Override
	public <D extends Data> D readData(Class<D> readClass, FileFilter format,
			InputStream in) throws ReadException {
		return m_ReaderManager.readData(readClass, format, in);
	}

	@Override
	public Collection<Class<? extends Data>> getWritableDataClasses() {
		return m_WriterManager.getWritableDataClasses();
	}

	@Override
	public List<FileNameExtensionFilter> getWriteFormats(
			Class<? extends Data> writeClass) {
		return m_WriterManager.getWriteFormats(writeClass);
	}

	@Override
	public void writeData(Data d, FileNameExtensionFilter format,
			OutputStream out) throws WriteException {
		m_WriterManager.writeData(d, format, out);
	}




	
}
