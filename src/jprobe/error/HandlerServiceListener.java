package jprobe.error;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;

public class HandlerServiceListener implements ServiceListener{
	
	private BundleContext context;
	private Collection<ErrorHandler> handlers;
	
	public HandlerServiceListener(BundleContext context){
		this.context = context;
		handlers = new LinkedHashSet<ErrorHandler>();
	}
	
	@Override
	public void serviceChanged(ServiceEvent ev) {
		// TODO Auto-generated method stub
		ServiceReference ref = ev.getServiceReference();
		Object service = context.getService(ref);
		if(service instanceof ErrorHandler){
			ErrorHandler handler = (ErrorHandler) service;
			switch(ev.getType()){
			case ServiceEvent.REGISTERED:
				handlers.add(handler);
				break;
			case ServiceEvent.UNREGISTERING:
				handlers.remove(handler);
				break;
			default:
				break;
			}
		}
	}
	
	public Collection<ErrorHandler> getRegisteredErrorHandlers(){
		return Collections.unmodifiableCollection(handlers);
	}

}
