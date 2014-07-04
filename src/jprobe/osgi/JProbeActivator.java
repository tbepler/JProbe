package jprobe.osgi;

import java.util.HashMap;
import java.util.Map;

import jprobe.JProbe;
import jprobe.services.AbstractServiceListener;
import jprobe.services.CorePluginService;
import jprobe.services.JProbeCore;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JProbeActivator extends AbstractServiceListener<CorePluginService> implements BundleActivator{
	
	private static Logger LOG = LoggerFactory.getLogger(JProbeActivator.class);
	private static Bundle m_Bundle = null;
	
	public static Bundle getBundle(){
		return m_Bundle;
	}
	
	private final Map<CorePluginService, JProbeCore> m_CoreContexts = new HashMap<CorePluginService, JProbeCore>();
	
	private final AbstractServiceListener<?>[] m_Listeners;
	private final JProbe m_Core;
	
	private BundleContext m_Context = null;
	private ServiceRegistration<JProbeCore> m_Registration = null;
	
	public JProbeActivator(JProbe core, AbstractServiceListener<?> ... listeners){
		super(CorePluginService.class);
		m_Core = core;
		m_Listeners = listeners;
	}

	@Override
	public void start(BundleContext context) throws Exception {
		LOG.info("Starting core bundle: {}", context.getBundle());
		m_Context = context;
		m_Bundle = m_Context.getBundle();
		this.load(context);
		m_Registration = context.registerService(JProbeCore.class, m_Core, null);
		for(AbstractServiceListener<?> l : m_Listeners){
			l.load(m_Context);
		}
		LOG.info("Started core bundle: {}", context.getBundle());
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		LOG.info("Stopping core bundle: {}", context.getBundle());
		if(m_Registration!=null){
			m_Registration.unregister();
		}
		for(AbstractServiceListener<?> l : m_Listeners){
			l.unload(context);
		}
		this.unload(context);
		m_Bundle = null;
		m_Context = null;
		LOG.info("Stopped core bundle: {}", context.getBundle());
	}
	
	public BundleContext getBundleContext(){
		return m_Context;
	}
	
	public Bundle[] getBundles(){
		if(m_Context!=null){
			return m_Context.getBundles();
		}
		return null;
	}

	@Override
	public void register(CorePluginService service, Bundle provider) {
		JProbeCore context = new BundleCoreContext(provider, m_Core);
		m_CoreContexts.put(service, context);
		//TODO
		service.start(context, );
	}

	@Override
	public void unregister(CorePluginService service, Bundle provider) {
		if(m_CoreContexts.containsKey(service)){
			JProbeCore context = m_CoreContexts.get(service);
			service.stop(context, );
			m_CoreContexts.remove(service);
		}
	}

}
