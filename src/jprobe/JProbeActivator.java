package jprobe;

import jprobe.services.AbstractServiceListener;
import jprobe.services.Debug;
import jprobe.services.JProbeCore;
import jprobe.services.JProbeLog;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class JProbeActivator implements BundleActivator{
	
	private static Bundle m_Bundle = null;
	
	public static Bundle getBundle(){
		return m_Bundle;
	}
	
	private final AbstractServiceListener<?>[] m_Listeners;
	private final JProbe m_Core;
	
	private BundleContext m_Context = null;
	private ServiceRegistration<JProbeCore> m_Registration = null;
	
	public JProbeActivator(JProbe core, AbstractServiceListener<?> ... listeners){
		m_Core = core;
		m_Listeners = listeners;
	}

	@Override
	public void start(BundleContext context) throws Exception {
		m_Context = context;
		m_Bundle = m_Context.getBundle();
		
		m_Registration = context.registerService(JProbeCore.class, m_Core, null);
		for(AbstractServiceListener<?> l : m_Listeners){
			l.load(m_Context);
		}
		if(Debug.getLevel() == Debug.FULL || Debug.getLevel() == Debug.LOG){
			JProbeLog.getInstance().write(JProbeActivator.getBundle(), "JProbe bundle started.");
		}
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		if(m_Registration!=null){
			m_Registration.unregister();
		}
		for(AbstractServiceListener<?> l : m_Listeners){
			l.unload(context);
		}
		
		if(Debug.getLevel() == Debug.FULL || Debug.getLevel() == Debug.LOG){
			JProbeLog.getInstance().write(JProbeActivator.getBundle(), "JProbe bundle stopped.");
		}
		
		m_Bundle = null;
		m_Context = null;
		
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

}
