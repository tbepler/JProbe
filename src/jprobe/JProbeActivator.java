package jprobe;

import jprobe.services.JProbeCore;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class JProbeActivator implements BundleActivator{
	
	private static Bundle m_Bundle = null;
	
	private BundleContext m_Context = null;
	private JProbeCore m_Core = null;
	private ServiceRegistration<JProbeCore> m_Registration = null;
	
	JProbeActivator(JProbeCore core){
		this.m_Core = core;
	}
	
	public static Bundle getBundle(){
		return m_Bundle;
	}

	@Override
	public void start(BundleContext context) throws Exception {
		m_Context = context;
		m_Bundle = m_Context.getBundle();
		m_Registration = context.registerService(JProbeCore.class, m_Core, null);
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		if(m_Registration!=null){
			m_Registration.unregister();
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
