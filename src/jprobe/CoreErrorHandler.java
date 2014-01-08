package jprobe;

import java.util.Collection;
import java.util.LinkedHashSet;

import jprobe.services.error.ErrorHandler;
import jprobe.log.Log;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;

public class CoreErrorHandler implements ErrorHandler, ServiceListener{
	
	private BundleContext context;
	private Collection<ErrorHandler> handlers;
	private Log log;
	
	public CoreErrorHandler(BundleContext context, Log log){
		this.context = context;
		this.log = log;
		handlers = new LinkedHashSet<ErrorHandler>();
		context.addServiceListener(this);
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
	
	void addErrorHandler(ErrorHandler handler){
		handlers.add(handler);
	}
	
	void removeErrorHandler(ErrorHandler handler){
		handlers.remove(handler);
	}

	@Override
	public void handleException(Bundle reporter, Exception e) {
		log.write(reporter, "ERROR: "+e.getMessage());
		for(ErrorHandler handler : handlers){
			handler.handleException(reporter, e);
		}
	}

	@Override
	public void handleException(Bundle reporter, String message) {
		log.write(reporter, "ERROR: "+message);
		for(ErrorHandler handler : handlers){
			handler.handleException(reporter, message);
		}
	}

}
