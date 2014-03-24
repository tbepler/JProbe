package jprobe.services;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;

public abstract class AbstractServiceListener<S> implements ServiceListener{

	private Class<S> m_Target;
	private BundleContext m_Context;
	
	public AbstractServiceListener(Class<S> listenFor, BundleContext context){
		m_Target = listenFor;
		m_Context = context;
	}
	
	public AbstractServiceListener<S> load(){
		m_Context.addServiceListener(this);
		try {
			for(ServiceReference<S> ref : m_Context.getServiceReferences(m_Target, null)){
				this.serviceChanged(new ServiceEvent(ServiceEvent.REGISTERED, ref));
			}
		} catch (InvalidSyntaxException e) {
			//do nothing
		}
		return this;
	}
	
	public AbstractServiceListener<S> unload(){
		m_Context.removeServiceListener(this);
		return this;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void serviceChanged(ServiceEvent ev) {
		ServiceReference<?> sr = ev.getServiceReference();
		Bundle bundle = sr.getBundle();
		Object service = m_Context.getService(sr);
		if(m_Target.isAssignableFrom(service.getClass())){
			switch(ev.getType()) {
			case ServiceEvent.REGISTERED:
				register((S) service, bundle);
				break;
			case ServiceEvent.UNREGISTERING:
				unregister((S) service, bundle);
				break;
			default:
				break;
			}
		}
	}
	
	public abstract void register(S service, Bundle provider);
	public abstract void unregister(S service, Bundle provider);
	
}
