package plugins.functionMenu;

import jprobe.services.JProbeCore;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

public class Activator implements BundleActivator{
	
	private JProbeCore core;
	private FunctionMenu menu;
	
	@Override
	public void start(BundleContext context) throws Exception {
		ServiceReference ref = context.getServiceReference(JProbeCore.class);
		core = (JProbeCore) context.getService(ref);
		menu = new FunctionMenu(core);
		core.addDropdownMenu(menu, context.getBundle());
		System.out.println("Function menu started");
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		menu.cleanup();
		core.removeDropdownMenu(menu, context.getBundle());
	}

}
