package plugins.jprobe.gui;

import jprobe.services.ErrorHandler;
import jprobe.services.JProbeCore;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;

import plugins.jprobe.gui.services.GUIErrorManager;
import plugins.jprobe.gui.services.JProbeGUI;

public class GUIActivator implements BundleActivator{
	
	private static Bundle m_Bundle = null;
	
	private JProbeCore m_Core;
	private JProbeGUIFrame m_Gui;
	private GUIErrorManager m_ErrorManager = null;
	private ServiceRegistration<JProbeGUI> m_Registration = null;
	
	public static Bundle getBundle(){
		return m_Bundle;
	}
	
	@Override
	public void start(BundleContext context) throws Exception {
		m_Bundle = context.getBundle();
		ServiceReference ref = context.getServiceReference(JProbeCore.class);
		m_Core = (JProbeCore) context.getService(ref);
		m_Gui = new JProbeGUIFrame(m_Core, "JProbe", context.getBundle(), new GUIConfig(Constants.CONFIG_FILE));
		m_Gui.setVisible(true);
		m_ErrorManager = new GUIErrorManager(m_Gui);
		ErrorHandler.getInstance().addErrorManager(m_ErrorManager);
		m_Registration = context.registerService(JProbeGUI.class, m_Gui, null);
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		if(m_Registration != null){
			m_Registration.unregister();
		}
		ErrorHandler.getInstance().removeErrorManager(m_ErrorManager);
		m_Gui.dispose();
		m_Bundle = null;
	}

}
