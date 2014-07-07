package jprobe.osgi;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
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
import jprobe.framework.view.PersistentView;
import jprobe.osgi.services.AbstractServiceListener;
import jprobe.osgi.services.ControllerResource;
import jprobe.osgi.services.ModelResource;
import jprobe.osgi.services.ViewResource;

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
	private final AbstractServiceListener<ViewResource> m_ViewList = createViewResourceListener();
	
	private final BundleActivator m_Activator = new SystemActivator(m_ModelList, m_ControllerList, m_ViewList);
	
	private Felix m_Felix = null;
	private ModelResource m_ModelCons = null;
	private ControllerResource m_ControllerCons = null;
	private ViewResource m_ViewCons = null;
	
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
		return m_ModelCons.newModel();
	}

	@Override
	public Controller newController() {
		return m_ControllerCons.newController();
	}

	@Override
	public PersistentView newView() {
		return m_ViewCons.newView();
	}
	

	private AbstractServiceListener<ModelResource> createModelResourceListener() {
		return new AbstractServiceListener<ModelResource>(ModelResource.class){

			@Override
			public void register(ModelResource service, Bundle provider) {
				if(m_ModelCons == null){
					LOG.info("{}: {} registered by bundle: {}", ModelResource.class, service, provider);
					m_ModelCons = service;
				}else{
					LOG.warn("Multiple models detected. {}: {} registered by bundle: {}. Current {}: {}", ModelResource.class, service, provider, ModelResource.class, m_ModelCons);
				}
			}

			@Override
			public void unregister(ModelResource service, Bundle provider) {
				if(m_ModelCons == service){
					LOG.info("{}: {} unregistered by bundle: {}", ModelResource.class, service, provider);
					m_ModelCons = null;
				}
			}
			
		};
	}
	
	private AbstractServiceListener<ControllerResource> createControllerResourceListener() {
		return new AbstractServiceListener<ControllerResource>(ControllerResource.class){

			@Override
			public void register(ControllerResource service, Bundle provider) {
				if(m_ControllerCons == null){
					LOG.info("{}: {} registered by bundle: {}", ControllerResource.class, service, provider);
					m_ControllerCons = service;
				}else{
					LOG.warn("Multiple controllers detected. {}: {} registered by bundle: {}. Current {}: {}", ControllerResource.class, service, provider, ControllerResource.class, m_ControllerCons);
				}
			}

			@Override
			public void unregister(ControllerResource service, Bundle provider) {
				if(m_ControllerCons == service){
					LOG.info("{}: {} unregistered by bundle: {}", ControllerResource.class, service, provider);
					m_ControllerCons = null;
				}
			}
			
		};
	}
	
	private AbstractServiceListener<ViewResource> createViewResourceListener() {
		return new AbstractServiceListener<ViewResource>(ViewResource.class){

			@Override
			public void register(ViewResource service, Bundle provider) {
				if(m_ViewCons == null){
					LOG.info("{}: {} registered by bundle: {}", ViewResource.class, service, provider);
					m_ViewCons = service;
				}else{
					LOG.warn("Multiple views detected. {}: {} registered by bundle: {}. Current {}: {}", ViewResource.class, service, provider, ViewResource.class, m_ViewCons);
				}
			}

			@Override
			public void unregister(ViewResource service, Bundle provider) {
				if(m_ViewCons == service){
					LOG.info("{}: {} unregistered by bundle: {}", ViewResource.class, service, provider);
					m_ViewCons = null;
				}
			}
			
		};
	}
	
}
