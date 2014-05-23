package jprobe;

import jprobe.services.Debug;
import jprobe.services.JProbeCore;
import jprobe.services.Log;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceRegistration;

public class JProbeActivator implements BundleActivator{
	
	private static Bundle m_Bundle = null;
	
	private BundleContext m_Context = null;
	private ServiceListener[] m_Listeners;
	private JProbe m_Core = null;
	private ServiceRegistration<JProbeCore> m_Registration = null;
	private CoreFunctionManager m_FncManager = null;
	private CoreDataManager m_DataManager = null;
	
	JProbeActivator(JProbe core, ServiceListener ... listeners){
		this.m_Core = core;
		m_Listeners = listeners;
	}
	
	public static Bundle getBundle(){
		return m_Bundle;
	}

	@Override
	public void start(BundleContext context) throws Exception {
		m_Context = context;
		m_Bundle = m_Context.getBundle();
		m_FncManager = new CoreFunctionManager(m_Core, context);
		m_Core.setFunctionManager(m_FncManager);
		m_FncManager.load();
		m_DataManager = new CoreDataManager(m_Core, context);
		m_Core.setDataManager(m_DataManager);
		m_DataManager.load();
		m_Registration = context.registerService(JProbeCore.class, m_Core, null);
		for(ServiceListener l : m_Listeners){
			m_Context.addServiceListener(l);
		}
		if(Debug.getLevel() == Debug.FULL || Debug.getLevel() == Debug.LOG){
			Log.getInstance().write(JProbeActivator.getBundle(), "JProbe started.");
		}
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		if(m_Registration!=null){
			m_Registration.unregister();
		}
		if(m_FncManager != null){
			m_FncManager.unload();
			m_FncManager = null;
		}
		if(m_DataManager != null){
			m_DataManager.unload();
			m_DataManager = null;
		}
		for(ServiceListener l : m_Listeners){
			m_Context.removeServiceListener(l);
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
