package plugins.testDataAndFunction;

import jprobe.services.JProbeCore;
import jprobe.services.function.Function;

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
		core.getFunctionManager().addFunction(fun, context.getBundle());
		System.out.println("Started test data and function plugin");
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		core.getFunctionManager().removeFunction(fun, context.getBundle());
	}

}
