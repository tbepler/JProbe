package plugins.functions.gui;

import java.util.Collection;

import jprobe.services.Debug;
import jprobe.services.JProbeCore;
import jprobe.services.Log;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;

import plugins.jprobe.gui.services.JProbeGUI;

public class Activator implements BundleActivator{
	
	private static Bundle BUNDLE = null;
	private static JProbeGUI GUI;
	
	private JProbeCore core;
	private FunctionMenu menu;
	private BundleContext bc;

	private ServiceListener sl = new ServiceListener() {
		@Override
		public void serviceChanged(ServiceEvent ev) {
			ServiceReference<?> sr = ev.getServiceReference();
			switch(ev.getType()) {
				case ServiceEvent.REGISTERED:
					GUI = (JProbeGUI) bc.getService(sr);
					core = GUI.getJProbeCore();
					initMenu();
					break;
				default:
					break;
			}
		}
	};
	
	public static Bundle getBundle(){
		return BUNDLE;
	}
	
	public static JProbeGUI getGUI(){
		return GUI;
	}
	
	private void initMenu(){
		menu = new FunctionMenu( GUI.getGUIFrame(), core, bc.getBundle());
		GUI.addDropdownMenu(menu, bc.getBundle());
		if(Debug.getLevel() == Debug.FULL || Debug.getLevel() == Debug.LOG){
			Log.getInstance().write(BUNDLE, "Function menu started.");
		}
	}
	
	@Override
	public void start(BundleContext context) throws Exception {
		bc = context;
		BUNDLE = context.getBundle();
		String filter = "(objectclass="+JProbeGUI.class.getName()+")";
		context.addServiceListener(sl, filter);
		Collection<ServiceReference<JProbeGUI>> refs = context.getServiceReferences(JProbeGUI.class, null);
		for(ServiceReference<?> r : refs){
			sl.serviceChanged(new ServiceEvent(ServiceEvent.REGISTERED, r));
		}
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		if(menu != null){
			menu.cleanup();
			if(GUI != null){
				GUI.removeDropdownMenu(menu, context.getBundle());
			}
		}
		if(GUI != null){
			GUI = null;
		}
		if(Debug.getLevel() == Debug.FULL || Debug.getLevel() == Debug.LOG){
			Log.getInstance().write(BUNDLE, "Function menu stopped.");
		}
	}

}
