package plugins.genome;

import jprobe.services.JProbeCore;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;

import plugins.genome.gui.GenomeMenu;
import plugins.jprobe.gui.services.JProbeGUI;

public class GenomeActivator implements BundleActivator{

	private static Bundle BUNDLE = null;
	
	public static Bundle getBundle(){
		return BUNDLE;
	}
	
	private JProbeCore m_Core = null;
	private JProbeGUI m_Gui = null;
	private ServiceListener m_GuiListener = null;
	
	@Override
	public void start(BundleContext bc) throws Exception {
		BUNDLE = bc.getBundle();
		m_GuiListener = new AbstractServiceListener<JProbeGUI>(JProbeGUI.class, bc){
			@Override
			public void register(JProbeGUI service) {
				GenomeActivator.this.register(service);
			}
			@Override
			public void unregister(JProbeGUI service) {
				GenomeActivator.this.unregister(service);
			}
		};
		bc.addServiceListener(m_GuiListener);
		ServiceReference<JProbeCore> sr = bc.getServiceReference(JProbeCore.class);
		m_Core = bc.getService(sr);
		for(ServiceReference<JProbeGUI> guiReference : bc.getServiceReferences(JProbeGUI.class, null)){
			m_GuiListener.serviceChanged(new ServiceEvent(ServiceEvent.REGISTERED, guiReference));
		}
	}

	@Override
	public void stop(BundleContext bc) throws Exception {
		if(m_GuiListener != null){
			bc.removeServiceListener(m_GuiListener);
			m_GuiListener = null;
		}
		if(m_Gui != null){
			this.unregister(m_Gui);
		}
		if(m_Core != null){
			m_Core = null;
		}
		if(BUNDLE != null){
			BUNDLE = null;
		}
	}
	
	private GenomeMenu m_Menu = new GenomeMenu();
	
	private void register(JProbeGUI gui){
		m_Gui = gui;
		m_Gui.addDropdownMenu(m_Menu, getBundle());
		//add components and whatnot
	}
	
	private void unregister(JProbeGUI gui){
		if(m_Gui == gui){
			//remove components and whatnot
			m_Gui.removeDropdownMenu(m_Menu, getBundle());
			m_Gui = null;
		}
	}

}
