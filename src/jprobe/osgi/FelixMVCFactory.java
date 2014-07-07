package jprobe.osgi;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.felix.framework.Felix;
import org.apache.felix.framework.util.FelixConstants;
import org.apache.felix.main.AutoProcessor;
import org.apache.felix.main.Main;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import util.file.FileUtil;
import jprobe.framework.MVCFactory;
import jprobe.framework.controller.Controller;
import jprobe.framework.model.Model;
import jprobe.framework.view.BatchView;
import jprobe.framework.view.PersistentView;
import jprobe.osgi.services.AbstractServiceListener;
import jprobe.osgi.services.BatchViewResource;
import jprobe.osgi.services.ControllerResource;
import jprobe.osgi.services.ModelResource;
import jprobe.osgi.services.PersistentViewResource;

public class FelixMVCFactory implements MVCFactory{
	
	private static final Logger LOG = LoggerFactory.getLogger(FelixMVCFactory.class);
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static Map createFelixConfig(File felixCache, BundleActivator systemActivator) {
		Map felixConfig = new HashMap();
		//export the core service package
		felixConfig.put(Constants.FRAMEWORK_SYSTEMPACKAGES_EXTRA, OsgiConstants.FELIX_EXPORT_PACKAGES);
		felixConfig.put(Constants.FRAMEWORK_BOOTDELEGATION, OsgiConstants.FELIX_BOOTDELEGATION_PACKAGES);
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
		props.setProperty(AutoProcessor.AUTO_DEPLOY_DIR_PROPERY, OsgiConstants.PLUGIN_AUTODEPLOY);
		props.setProperty(AutoProcessor.AUTO_DEPLOY_ACTION_PROPERY, OsgiConstants.FELIX_AUTODEPLOY_ACTION);
		//set properties for the FileInstall bundle
		System.setProperty(OsgiConstants.FELIX_FILE_INSTALL_DIR_PROP, userPluginDir.getAbsolutePath());
		System.setProperty(OsgiConstants.FELIX_FILE_INSTALL_INITIALDELAY_PROP, OsgiConstants.FELIX_INITIALDELAY);
		return props;
	}
	
	private static File initializeFelixCacheDirectory(File userDir){
		//assign the cache directory a unique name, because it will be deleted when the program exits
		File cacheDir = FileUtil.createUniqueFile(userDir, OsgiConstants.FELIX_CACHE_DIR_NAME);
		if(!cacheDir.mkdir()){
			LOG.warn("Unable to initialize temporary felix bundle-cache {}, trying in working directory...", cacheDir);
			cacheDir = FileUtil.createUniqueFile(OsgiConstants.FELIX_CACHE_DIR_NAME);
			if(!cacheDir.mkdir()){
				LOG.error("Unable to initialize felix bundle-cache in {} or working directory. Exiting.", userDir);
				System.exit(1);
			}else{
				LOG.info("Created temporary felix bundle-cache {}", cacheDir);
			}
		}else{
			LOG.info("Created temporary felix bundle-cache {}", cacheDir);
		}
		cacheDir.deleteOnExit();
		return cacheDir;
	}
	
	private static File initializeUserPluginDirectory(File userDir){
		File logDir = new File(userDir, OsgiConstants.USER_PLUGINS_DIR_NAME);
		if(!logDir.exists() && !logDir.mkdir()){
			LOG.warn("Unable to initialize user plugins directory {}", logDir);
		}
		return logDir;
	}
	
	private final AbstractServiceListener<ModelResource> m_ModelList = createModelResourceListener();
	private final AbstractServiceListener<ControllerResource> m_ControllerList = createControllerResourceListener();
	private final AbstractServiceListener<PersistentViewResource> m_PersistentViewList = createPersistentViewResourceListener();
	private final AbstractServiceListener<BatchViewResource> m_BatchViewList = createBatchViewResourceListener();
	
	private final BundleActivator m_Activator = new SystemActivator(m_ModelList, m_ControllerList, m_PersistentViewList, m_BatchViewList);
	
	private final LinkedList<ModelResource> m_ModelCons = new LinkedList<ModelResource>();
	private final LinkedList<ControllerResource> m_ControllerCons = new LinkedList<ControllerResource>();
	private final LinkedList<PersistentViewResource> m_PersistentViewCons = new LinkedList<PersistentViewResource>();
	private final LinkedList<BatchViewResource> m_BatchViewCons = new LinkedList<BatchViewResource>();
	
	private Felix m_Felix = null;
	
	@Override
	public void start(Properties props) {
		//read the user directory from properties and init the felix cache directory and the user's plugin directory
		File userDir = new File(props.getProperty(jprobe.Constants.PROPERTY_USER_DIR));
		File cacheDir = initializeFelixCacheDirectory(userDir);
		File pluginDir = initializeUserPluginDirectory(userDir);
		//create felix config map
		@SuppressWarnings("rawtypes")
		Map felixConfig = createFelixConfig(cacheDir, m_Activator);
		try{
			//create an instance of the felix framework using the config map
			m_Felix = new Felix(felixConfig);
			//get properties to pass the felix
			Properties felixProps = initSystemProperties(pluginDir);
			LOG.info("Initializing Apache Felix framework...");
			m_Felix.init();
			LOG.info("Apace Felix framework initialized.");
			AutoProcessor.process(felixProps, m_Felix.getBundleContext());
			//start the felix instance
			LOG.info("Starting Apache Felix framework...");
			m_Felix.start();
			LOG.info("Apache Felix framework started.");
		} catch (Exception e){
			LOG.error("Error creating Apache Felix framework: {}", e);
			//throw a runtime exception if the framework could not be properly initialized
			throw new RuntimeException(e);
		}
	}

	@Override
	public void stop(Properties props) {
		if(m_Felix != null){
			try{
				LOG.info("Stopping Apache Felix framework...");
				m_Felix.stop();
				m_Felix.waitForStop(0);
				m_Felix = null;
				LOG.info("Apache Felix framework stopped...");
			} catch (Exception e){
				LOG.error("Error stopping Apache Felix framework: {}",e);
			}
		}
	}

	@Override
	public void waitForStop(long timeout) throws InterruptedException {
		if(m_Felix != null){
			m_Felix.waitForStop(timeout);
		}
	}

	@Override
	public Model newModel() {
		if(m_ModelCons.isEmpty()){
			return null;
		}
		return m_ModelCons.peek().newModel();
	}

	@Override
	public Model newModel(String id) {
		for(ModelResource cons : m_ModelCons){
			if(cons.uniqueId().equals(id)){
				return cons.newModel();
			}
		}
		return null;
	}

	@Override
	public Controller newController() {
		if(m_ControllerCons.isEmpty()){
			return null;
		}
		return m_ControllerCons.peek().newController();
	}

	@Override
	public Controller newController(String id) {
		for(ControllerResource cons : m_ControllerCons){
			if(cons.uniqueId().equals(id)){
				return cons.newController();
			}
		}
		return null;
	}
	
	@Override
	public PersistentView newPersistentView() {
		if(m_PersistentViewCons.isEmpty()){
			return null;
		}
		return m_PersistentViewCons.peek().newPersistentView();
	}

	@Override
	public PersistentView newPersistentView(String id) {
		for(PersistentViewResource cons : m_PersistentViewCons){
			if(cons.uniqueId().equals(id)){
				return cons.newPersistentView();
			}
		}
		return null;
	}

	@Override
	public BatchView newBatchView() {
		if(m_BatchViewCons.isEmpty()){
			return null;
		}
		return m_BatchViewCons.peek().newBatchtView();
	}

	@Override
	public BatchView newBatchView(String id) {
		for(BatchViewResource cons : m_BatchViewCons){
			if(cons.uniqueId().equals(id)){
				return cons.newBatchtView();
			}
		}
		return null;
	}
	

	private AbstractServiceListener<ModelResource> createModelResourceListener() {
		return new AbstractServiceListener<ModelResource>(ModelResource.class){

			@Override
			public void register(ModelResource service, Bundle provider) {
				if(m_ModelCons.add(service)){
					LOG.info("{}: {} registered by bundle: {}", ModelResource.class, service, provider);
				}
			}

			@Override
			public void unregister(ModelResource service, Bundle provider) {
				if(m_ModelCons.remove(service)){
					LOG.info("{}: {} unregistered by bundle: {}", ModelResource.class, service, provider);
				}
			}
			
		};
	}
	
	private AbstractServiceListener<ControllerResource> createControllerResourceListener() {
		return new AbstractServiceListener<ControllerResource>(ControllerResource.class){

			@Override
			public void register(ControllerResource service, Bundle provider) {
				if(m_ControllerCons.add(service)){
					LOG.info("{}: {} registered by bundle: {}", ControllerResource.class, service, provider);
				}
			}

			@Override
			public void unregister(ControllerResource service, Bundle provider) {
				if(m_ControllerCons.remove(service)){
					LOG.info("{}: {} unregistered by bundle: {}", ControllerResource.class, service, provider);
				}
			}
			
		};
	}
	
	private AbstractServiceListener<PersistentViewResource> createPersistentViewResourceListener() {
		return new AbstractServiceListener<PersistentViewResource>(PersistentViewResource.class){

			@Override
			public void register(PersistentViewResource service, Bundle provider) {
				if(m_PersistentViewCons.add(service)){
					LOG.info("{}: {} registered by bundle: {}", PersistentViewResource.class, service, provider);
				}
			}

			@Override
			public void unregister(PersistentViewResource service, Bundle provider) {
				if(m_PersistentViewCons.remove(service)){
					LOG.info("{}: {} unregistered by bundle: {}", PersistentViewResource.class, service, provider);
				}
			}
			
		};
	}
	
	private AbstractServiceListener<BatchViewResource> createBatchViewResourceListener() {
		return new AbstractServiceListener<BatchViewResource>(BatchViewResource.class){

			@Override
			public void register(BatchViewResource service, Bundle provider) {
				if(m_BatchViewCons.add(service)){
					LOG.info("{}: {} registered by bundle: {}", BatchViewResource.class, service, provider);
				}
			}

			@Override
			public void unregister(BatchViewResource service, Bundle provider) {
				if(m_BatchViewCons.remove(service)){
					LOG.info("{}: {} unregistered by bundle: {}", BatchViewResource.class, service, provider);
				}
			}
			
		};
	}

	
}
