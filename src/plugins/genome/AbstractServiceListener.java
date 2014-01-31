package plugins.genome;

import org.osgi.framework.BundleContext;
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
	
	@SuppressWarnings("unchecked")
	@Override
	public void serviceChanged(ServiceEvent ev) {
		ServiceReference<?> sr = ev.getServiceReference();
		Object service = m_Context.getService(sr);
		if(m_Target.isAssignableFrom(service.getClass())){
			switch(ev.getType()) {
			case ServiceEvent.REGISTERED:
				register((S) service);
				break;
			case ServiceEvent.UNREGISTERING:
				unregister((S) service);
				break;
			default:
				break;
			}
		}
	}
	
	public abstract void register(S service);
	public abstract void unregister(S service);
	
}
