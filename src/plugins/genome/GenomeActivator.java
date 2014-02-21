package plugins.genome;

import jprobe.services.AbstractServiceListener;
import jprobe.services.JProbeCore;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;

import plugins.genome.gui.GenomeMenu;
import plugins.genome.services.GenomeFunction;
import plugins.genome.services.GenomeService;
import plugins.jprobe.gui.services.JProbeGUI;

public class GenomeActivator implements BundleActivator{

	private static Bundle BUNDLE = null;
	
	public static Bundle getBundle(){
		return BUNDLE;
	}
	
	private JProbeCore m_Core = null;
	private JProbeGUI m_Gui = null;
	private ServiceListener m_GuiListener = null;
	private GenomeService m_Service = null;
	private ServiceRegistration<GenomeService> m_Registration = null;
	private ServiceListener m_PrototypeListener = null;
	
	@Override
	public void start(BundleContext bc) throws Exception {
		BUNDLE = bc.getBundle();
		m_GuiListener = new AbstractServiceListener<JProbeGUI>(JProbeGUI.class, bc){
			
			@Override
			public void register(JProbeGUI service, Bundle provider) {
				GenomeActivator.this.register(service);
			}
			
			@Override
			public void unregister(JProbeGUI service, Bundle provider) {
				GenomeActivator.this.unregister(service);
			}
			
		};
		bc.addServiceListener(m_GuiListener);
		ServiceReference<JProbeCore> sr = bc.getServiceReference(JProbeCore.class);
		m_Core = bc.getService(sr);
		for(ServiceReference<JProbeGUI> guiReference : bc.getServiceReferences(JProbeGUI.class, null)){
			m_GuiListener.serviceChanged(new ServiceEvent(ServiceEvent.REGISTERED, guiReference));
		}
		m_Service = new GenomeCore();
		m_Registration = bc.registerService(GenomeService.class, m_Service, null);
		m_PrototypeListener = new AbstractServiceListener<GenomeFunction>(GenomeFunction.class, bc){

			@Override
			public void register(GenomeFunction service, Bundle provider) {
				m_Service.addGenomeFunction(service, provider);
			}

			@Override
			public void unregister(GenomeFunction service, Bundle provider) {
				m_Service.removeGenomeFunction(service, provider);
			}
			
		};
		bc.addServiceListener(m_PrototypeListener);
		for(ServiceReference<GenomeFunction> genomeFunctionReference : bc.getServiceReferences(GenomeFunction.class, null)){
			m_PrototypeListener.serviceChanged(new ServiceEvent(ServiceEvent.REGISTERED, genomeFunctionReference));
		}
	}

	@Override
	public void stop(BundleContext bc) throws Exception {
		if(m_Registration != null){
			m_Registration.unregister();
			m_Registration = null;
		}
		if(m_Service != null){
			m_Service = null;
		}
		if(m_PrototypeListener != null){
			bc.removeServiceListener(m_PrototypeListener);
			m_PrototypeListener = null;
		}
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
	
	private GenomeMenu m_Menu = null;
	
	private void register(JProbeGUI gui){
		m_Gui = gui;
		m_Menu = new GenomeMenu(m_Gui.getGUIFrame(), m_Core, m_Service);
		m_Gui.addDropdownMenu(m_Menu, getBundle());
		//add components and whatnot
	}
	
	private void unregister(JProbeGUI gui){
		if(m_Gui == gui){
			//remove components and whatnot
			m_Gui.removeDropdownMenu(m_Menu, getBundle());
			m_Menu.cleanup();
			m_Menu = null;
			m_Gui = null;
		}
	}

}
