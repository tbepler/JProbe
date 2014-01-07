package plugins.dataviewer.gui;

import java.util.Collection;

import jprobe.services.JProbeCore;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;

import plugins.functions.gui.FunctionMenu;
import plugins.jprobe.gui.services.JProbeGUI;

public class Activator implements BundleActivator{

	private JProbeCore core;
	private JProbeGUI gui;
	private BundleContext bc;
	private DataTabPane tabPane;

	private ServiceListener sl = new ServiceListener() {
		@Override
		public void serviceChanged(ServiceEvent ev) {
			ServiceReference sr = ev.getServiceReference();
			switch(ev.getType()) {
				case ServiceEvent.REGISTERED:
					gui = (JProbeGUI) bc.getService(sr);
					core = gui.getJProbeCore();
					initTabPane();
					break;
				default:
					break;
			}
		}
	};
	
	private void initTabPane(){
		tabPane = new DataTabPane(core.getDataManager());
		gui.addComponent(tabPane, tabPane.getGridBagConstraints(), bc.getBundle());
		System.out.println("DataViewer started");
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
		tabPane.cleanup();
		gui.removeComponent(tabPane, context.getBundle());;
	}

}
