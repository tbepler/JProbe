package jprobe.system.osgi;

import jprobe.system.osgi.services.AbstractServiceListener;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class SystemActivator implements BundleActivator{
	
	private final AbstractServiceListener<?>[] m_Listeners;
	
	public SystemActivator(AbstractServiceListener<?> ... listeners){
		m_Listeners = listeners;
	}
	
	@Override
	public void start(BundleContext context) throws Exception {
		for(AbstractServiceListener<?> l : m_Listeners){
			l.load(context);
		}
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		for(AbstractServiceListener<?> l : m_Listeners){
			l.unload(context);
		}
	}
	
	
	
}
