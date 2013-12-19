package plugins.functions.gui;

import java.util.Collection;

import jprobe.services.JProbeCore;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;

import plugins.jprobe.gui.services.JProbeGUI;

public class Activator implements BundleActivator{
	
	private JProbeCore core;
	private JProbeGUI gui;
	private FunctionMenu menu;
	private BundleContext bc;

	private ServiceListener sl = new ServiceListener() {
		@Override
		public void serviceChanged(ServiceEvent ev) {
			ServiceReference sr = ev.getServiceReference();
			switch(ev.getType()) {
				case ServiceEvent.REGISTERED:
					gui = (JProbeGUI) bc.getService(sr);
					core = gui.getJProbeCore();
					initMenu();
					break;
				default:
					break;
			}
		}
	};
	
	private void initMenu(){
		menu = new FunctionMenu(core);
		gui.addDropdownMenu(menu, bc.getBundle());
		System.out.println("Function menu started");
	}
	
	@Override
	public void start(BundleContext context) throws Exception {
		bc = context;
		String filter = "(objectclass="+JProbeGUI.class.getName()+")";
		context.addServiceListener(sl, filter);
		Collection<ServiceReference<JProbeGUI>> refs = context.getServiceReferences(JProbeGUI.class, null);
		for(ServiceReference r : refs){
			sl.serviceChanged(new ServiceEvent(ServiceEvent.REGISTERED, r));
		}
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		menu.cleanup();
		gui.removeDropdownMenu(menu, context.getBundle());
	}

}
