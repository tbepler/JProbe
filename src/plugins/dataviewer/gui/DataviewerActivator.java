package plugins.dataviewer.gui;

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

import plugins.dataviewer.gui.datalist.DataListPanel;
import plugins.functions.gui.FunctionMenu;
import plugins.jprobe.gui.services.JProbeGUI;

public class DataviewerActivator implements BundleActivator{
	
	private static Bundle BUNDLE = null;
	
	private JProbeCore m_Core;
	private JProbeGUI m_Gui;
	private BundleContext m_BC;
	private DataTabPane m_TabPane;
	private DataListPanel m_ListPanel;

	private ServiceListener sl = new ServiceListener() {
		@Override
		public void serviceChanged(ServiceEvent ev) {
			ServiceReference sr = ev.getServiceReference();
			switch(ev.getType()) {
				case ServiceEvent.REGISTERED:
					m_Gui = (JProbeGUI) m_BC.getService(sr);
					m_Core = m_Gui.getJProbeCore();
					init();
					break;
				default:
					break;
			}
		}
	};
	
	public static Bundle getBundle(){
		return BUNDLE;
	}
	
	private void init(){
		m_TabPane = new DataTabPane(m_Core.getDataManager());
		m_Gui.addComponent(m_TabPane, m_TabPane.getGridBagConstraints(), m_BC.getBundle());
		m_ListPanel = new DataListPanel(m_Core, m_Gui, m_TabPane);
		m_Gui.addComponent(m_ListPanel, m_ListPanel.getGridBagConstraints(), m_BC.getBundle());
		if(Debug.getLevel() == Debug.FULL || Debug.getLevel() == Debug.LOG){
			Log.getInstance().write(m_BC.getBundle(), "DataViewer started.");
		}
	}
	
	@Override
	public void start(BundleContext context) throws Exception {
		m_BC = context;
		BUNDLE = m_BC.getBundle();
		String filter = "(objectclass="+JProbeGUI.class.getName()+")";
		context.addServiceListener(sl, filter);
		Collection<ServiceReference<JProbeGUI>> refs = context.getServiceReferences(JProbeGUI.class, null);
		for(ServiceReference r : refs){
			sl.serviceChanged(new ServiceEvent(ServiceEvent.REGISTERED, r));
		}
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		m_TabPane.cleanup();
		m_Gui.removeComponent(m_TabPane, context.getBundle());
		m_ListPanel.cleanup();
		m_Gui.removeComponent(m_ListPanel, context.getBundle());
		if(Debug.getLevel() == Debug.FULL || Debug.getLevel() == Debug.LOG){
			Log.getInstance().write(m_BC.getBundle(), "DataViewer stopped.");
		}
	}

}
