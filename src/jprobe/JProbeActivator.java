package jprobe;

import jprobe.services.JProbeCore;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class JProbeActivator implements BundleActivator{
	
	private BundleContext context = null;
	private JProbeCore core = null;
	private ServiceRegistration<JProbeCore> registration = null;
	
	JProbeActivator(JProbeCore core){
		this.core = core;
	}

	@Override
	public void start(BundleContext context) throws Exception {
		this.context = context;
		registration = context.registerService(JProbeCore.class, core, null);
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		if(registration!=null){
			registration.unregister();
		}
		this.context = null;
	}
	
	public Bundle[] getBundles(){
		if(context!=null){
			return context.getBundles();
		}
		return null;
	}

}
