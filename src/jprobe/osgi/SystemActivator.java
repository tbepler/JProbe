package jprobe.osgi;

import jprobe.osgi.services.AbstractServiceListener;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SystemActivator implements BundleActivator{
	
	private static final Logger LOG = LoggerFactory.getLogger(SystemActivator.class);
	
	private final AbstractServiceListener<?>[] m_Listeners;
	
	public SystemActivator(AbstractServiceListener<?> ... listeners){
		m_Listeners = listeners;
	}
	
	@Override
	public void start(BundleContext context) throws Exception {
		for(AbstractServiceListener<?> l : m_Listeners){
			l.load(context);
		}
		LOG.info("System bundle: {} started.", context.getBundle());
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		for(AbstractServiceListener<?> l : m_Listeners){
			l.unload(context);
		}
		LOG.info("System bundle: {} stopped.", context.getBundle());
	}
	
	
	
}
