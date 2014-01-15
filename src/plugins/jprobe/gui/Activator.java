package plugins.jprobe.gui;

import java.awt.GraphicsEnvironment;
import java.awt.Point;

import jprobe.services.ErrorHandler;
import jprobe.services.JProbeCore;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;

import plugins.jprobe.gui.services.GUIErrorManager;
import plugins.jprobe.gui.services.JProbeGUI;

public class Activator implements BundleActivator{
	
	private JProbeCore m_Core;
	private JProbeGUIFrame m_Gui;
	private GUIErrorManager m_ErrorManager = null;
	private ServiceRegistration<JProbeGUI> m_Registration = null;
	
	@Override
	public void start(BundleContext context) throws Exception {
		ServiceReference ref = context.getServiceReference(JProbeCore.class);
		m_Core = (JProbeCore) context.getService(ref);
		m_Gui = new JProbeGUIFrame(m_Core, "JProbe", context.getBundle());
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
	}

}
