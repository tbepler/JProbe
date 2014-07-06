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

import jprobe.osgi.JProbeActivator;
import jprobe.services.CoreListener;
import jprobe.services.Workspace;
import jprobe.services.JProbeCore;
import jprobe.services.data.Data;
import jprobe.services.data.DataReader;
import jprobe.services.data.DataWriter;
import jprobe.services.data.ReadException;
import jprobe.services.data.WriteException;
import jprobe.services.function.Function;
import jprobe.system.model.FunctionManager;
import jprobe.system.model.ReaderManager;
import jprobe.system.model.WorkspaceManager;
import jprobe.system.model.WriterManager;

import org.apache.felix.framework.Felix;
import org.apache.felix.framework.util.FelixConstants;
import org.apache.felix.main.AutoProcessor;
import org.apache.felix.main.Main;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import util.save.LoadException;

public class JProbe implements JProbeCore{
	
	Logger LOG = LoggerFactory.getLogger(JProbe.class);
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static Map createFelixConfig(File felixCache, BundleActivator systemActivator) {
		Map felixConfig = new HashMap();
		//export the core service package
		felixConfig.put(Constants.FRAMEWORK_SYSTEMPACKAGES_EXTRA, jprobe.system.Constants.FELIX_EXPORT_PACKAGES);
		felixConfig.put(Constants.FRAMEWORK_BOOTDELEGATION, jprobe.system.Constants.FELIX_BOOTDELEGATION_PACKAGES);
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
		props.setProperty(AutoProcessor.AUTO_DEPLOY_DIR_PROPERY, jprobe.system.Constants.PLUGIN_AUTODEPLOY);
		props.setProperty(AutoProcessor.AUTO_DEPLOY_ACTION_PROPERY, jprobe.system.Constants.FELIX_AUTODEPLOY_ACTION);
		//set properties for the FileInstall bundle
		System.setProperty(jprobe.system.Constants.FELIX_FILE_INSTALL_DIR_PROP, userPluginDir.getAbsolutePath());
		System.setProperty(jprobe.system.Constants.FELIX_FILE_INSTALL_INITIALDELAY_PROP, jprobe.system.Constants.FELIX_INITIALDELAY);
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
	
	public JProbe(String userDir, String logsDir, String propsDir){
		
		m_UserDir = userDir;
		m_LogsDir = logsDir;
		m_PropertiesDir = propsDir;
		
		m_FunctionManager = new FunctionManager(this);
		m_ReaderManager = new ReaderManager(this);
		m_WriterManager = new WriterManager(this);
		m_WorkspaceManager = new WorkspaceManager(this);
		//create system bundle activator
		m_Activator = new JProbeActivator(this);

	}
	
	@SuppressWarnings({ "rawtypes" })
	public void start(File userPluginsDir, File cacheDir, String[] args){
		//check to make sure this hasn't already been started
		if(m_Felix != null){
			return;
		}
		//set the mode to GUI according to the passed arguments, command is default
		if(args.length > 0 && args[0].matches(jprobe.system.Constants.GUI_REGEX)){
			m_Mode = Mode.GUI;
		}
		LOG.info("Running in mode: {}", m_Mode);
		//create felix config map
		Map felixConfig = createFelixConfig(cacheDir, m_Activator);
		try{
			//create an instance of the felix framework using the config map
			m_Felix = new Felix(felixConfig);
			//get properties to pass the felix
			Properties props = initSystemProperties(userPluginsDir);
			LOG.info("Initializing Apache Felix framework...");
			m_Felix.init();
			LOG.info("Apace Felix framework initialized.");
			AutoProcessor.process(props, m_Felix.getBundleContext());
			//start the felix instance
			LOG.info("Starting Apache Felix framework...");
			m_Felix.start();
			LOG.info("Apache Felix framework started.");
		} catch (Exception e){
			LOG.error("Error creating Apache Felix framework: {}", e);
			//throw a runtime exception if the framework could not be properly initialized
			throw new RuntimeException(e);
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
		LOG.info("Parsing args: {}", (Object) args);
		Data d = ParsingEngine.parseAndExecute(System.err, m_FunctionManager, args);
		if(d != null){
			Collection<DataWriter<?>> writers = m_WriterManager.getDataWriters(d.getClass());
			boolean writeSuccesful = false;
			for(DataWriter<?> writer : writers){
				for(FileNameExtensionFilter format : writer.getWriteFormats()){
					try {
						LOG.info("Attempting to write Data: {} using Writer: {} and Format: {}", d.getClass(), writer.getClass(), format);
						OutputStream out = new BufferedOutputStream(System.out);
						this.writeData(writer, d, format, out);
						out.close();
						writeSuccesful = true;
						LOG.info("Wrote Data: {} using Writer: {} and Format {}", d.getClass(), writer.getClass(), format);
						return;
					} catch (Exception e) {
						LOG.info("Failed to write Data: {} using Writer: {} and Format {}. {}", d.getClass(), writer.getClass(), format, e);
					}
				}
			}
			if(!writeSuccesful){
				System.err.println("Unable to write "+d.getClass() + ".");
				LOG.error("Unable to write {}.", d.getClass());
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
		LOG.info("Core shutting down...");
		try{
			LOG.info("Stopping Apache Felix framework...");
			m_Felix.stop();
		} catch (Exception e){
			LOG.error("Error stopping Apache Felix framework: {}",e);
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
		return jprobe.system.Constants.NAME;
	}

	@Override
	public String getVersion() {
		return jprobe.system.Constants.VERSION;
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

	@Override
	public boolean addFunction(Function<?> f) {
		return m_FunctionManager.addFunction(f);
	}

	@Override
	public boolean removeFunction(Function<?> f) {
		return m_FunctionManager.removeFunction(f);
	}

	@Override
	public boolean addDataReader(DataReader<?> reader) {
		return m_ReaderManager.addDataReader(reader);
	}

	@Override
	public boolean removeDataReader(DataReader<?> reader) {
		return m_ReaderManager.removeDataReader(reader);
	}

	@Override
	public boolean addDataWriter(DataWriter<?> writer) {
		return m_WriterManager.addDataWriter(writer);
	}

	@Override
	public boolean removeDataWriter(DataWriter<?> writer) {
		return m_WriterManager.removeDataWriter(writer);
	}




	
}
