package jprobe.osgi.services;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;

public abstract class AbstractServiceListener<S> implements ServiceListener{
	
	private final Map<S, Bundle> m_Services = new HashMap<S, Bundle>();
	
	private Class<S> m_Target;
	private BundleContext m_Context;
	
	public AbstractServiceListener(Class<S> listenFor){
		m_Target = listenFor;
	}
	
	public AbstractServiceListener<S> load(BundleContext context){
		m_Context = context;
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
	
	public AbstractServiceListener<S> unload(BundleContext context){
		if(m_Context != null){
			m_Context.removeServiceListener(this);
			m_Context = null;
		}
		return this;
	}
	
	public AbstractServiceListener<S> unregisterAll(){
		for(Entry<S,Bundle> entry : m_Services.entrySet()){
			this.unregister(entry.getKey(), entry.getValue());
		}
		m_Services.clear();
		return this;
	}
	
	@Override
	public void serviceChanged(ServiceEvent ev) {
		ServiceReference<?> sr = ev.getServiceReference();
		Bundle bundle = sr.getBundle();
		if(m_Context != null){
			Object o = m_Context.getService(sr);
			if(m_Target.isAssignableFrom(o.getClass())){
				S service = m_Target.cast(o);
				switch(ev.getType()) {
				case ServiceEvent.REGISTERED:
					register(service, bundle);
					m_Services.put(service, bundle);
					break;
				case ServiceEvent.UNREGISTERING:
					unregister(service, bundle);
					m_Services.remove(service);
					break;
				default:
					break;
				}
			}
		}
	}
	
	public abstract void register(S service, Bundle provider);
	public abstract void unregister(S service, Bundle provider);
	
}
