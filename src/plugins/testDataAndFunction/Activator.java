package plugins.testDataAndFunction;

import jprobe.services.Function;
import jprobe.services.JProbeCore;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

public class Activator implements BundleActivator{
	
	private JProbeCore core;
	private Function fun = new TestFunction();
	
	@Override
	public void start(BundleContext context) throws Exception {
		ServiceReference ref = context.getServiceReference(JProbeCore.class);
		core = (JProbeCore) context.getService(ref);
		core.addFunction(fun, context.getBundle());
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		core.removeFunction(fun, context.getBundle());
	}

}
