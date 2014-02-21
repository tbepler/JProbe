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
	private JProbeCore m_Core = null;
	private ServiceRegistration<JProbeCore> m_Registration = null;
	private CommandManager m_CmdManager = null;
	
	JProbeActivator(JProbeCore core, ServiceListener ... listeners){
		this.m_Core = core;
		m_Listeners = listeners;
	}
	
	public static Bundle getBundle(){
		return m_Bundle;
	}
	
	public CommandManager getCommandManager(){
		return m_CmdManager;
	}

	@Override
	public void start(BundleContext context) throws Exception {
		m_Context = context;
		m_Bundle = m_Context.getBundle();
		m_CmdManager = new CommandManager(m_Context);
		m_Context.addServiceListener(m_CmdManager);
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
		if(m_CmdManager != null){
			m_Context.removeServiceListener(m_CmdManager);
			m_CmdManager = null;
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
