package plugins.jprobe.gui;

import java.util.ArrayList;
import java.util.Collection;

import jprobe.services.AbstractServiceListener;
import jprobe.services.ErrorHandler;
import jprobe.services.JProbeCore;
import jprobe.services.JProbeCore.Mode;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceListener;
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
	private Collection<AbstractServiceListener<?>> m_ServiceListeners = null;
	
	public static Bundle getBundle(){
		return m_Bundle;
	}
	
	@Override
	public void start(BundleContext context) throws Exception {
		m_Bundle = context.getBundle();
		ServiceReference ref = context.getServiceReference(JProbeCore.class);
		m_Core = (JProbeCore) context.getService(ref);
		if(m_Core.getMode() == Mode.COMMAND){
			return;
		}
		m_Gui = new JProbeGUIFrame(m_Core, "JProbe", context.getBundle(), new GUIConfig(Constants.CONFIG_FILE));
		m_Gui.setVisible(true);
		m_ErrorManager = new GUIErrorManager(m_Gui);
		ErrorHandler.getInstance().addErrorManager(m_ErrorManager);
		m_Registration = context.registerService(JProbeGUI.class, m_Gui, null);
		m_ServiceListeners = initServiceListeners(m_Gui, context);
	}
	
	private static Collection<AbstractServiceListener<?>> initServiceListeners(JProbeGUI gui, BundleContext context){
		Collection<AbstractServiceListener<?>> l = new ArrayList<AbstractServiceListener<?>>();
		l.add(new HelpTabListener(gui, context));
		l.add(new PreferencesTabListener(gui, context));
		l.add(new ComponentListener(gui, context));
		l.add(new MenuListener(gui, context));
		for(AbstractServiceListener<?> list : l){
			list.load();
		}
		return l;
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		if(m_Registration != null){
			m_Registration.unregister();
			m_Registration = null;
		}
		if(m_ErrorManager != null){
			ErrorHandler.getInstance().removeErrorManager(m_ErrorManager);
			m_ErrorManager = null;
		}
		if(m_Gui != null){
			m_Gui.dispose();
			m_Gui = null;
		}
		if(m_ServiceListeners != null){
			for(AbstractServiceListener<?> l : m_ServiceListeners){
				l.unload();
			}
			m_ServiceListeners = null;
		}
		m_Bundle = null;
	}

}
