package plugins.jprobe.gui;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.JFrame;

import jprobe.services.AbstractServiceListener;
import jprobe.services.ErrorHandler;
import jprobe.services.JProbeCore;
import jprobe.services.JProbeCore.Mode;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;

import plugins.jprobe.gui.services.GUIErrorManager;
import plugins.jprobe.gui.services.JProbeGUI;

public class GUIActivator implements BundleActivator{
	
	private static Bundle m_Bundle = null;
	private static JProbeGUIFrame m_Gui;
	
	private JProbeCore m_Core;
	private GUIConfig m_GuiConfig;
	private GUIErrorManager m_ErrorManager = null;
	private ServiceRegistration<JProbeGUI> m_Registration = null;
	private Collection<AbstractServiceListener<?>> m_ServiceListeners = null;
	
	public static Bundle getBundle(){
		return m_Bundle;
	}
	
	public static JFrame getFrame(){
		return m_Gui;
	}
	
	@Override
	public void start(BundleContext context) throws Exception {
		m_Bundle = context.getBundle();
		ServiceReference<JProbeCore> ref = context.getServiceReference(JProbeCore.class);
		m_Core = context.getService(ref);
		if(m_Core.getMode() != Mode.GUI){
			return;
		}
		File prefFile = new File(m_Core.getPreferencesDir() + File.separator + Constants.CONFIG_FILE_NAME);
		m_GuiConfig = new GUIConfig(prefFile);
		m_Gui = new JProbeGUIFrame(m_Core, "JProbe", context.getBundle(), m_GuiConfig);
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
			if(m_GuiConfig != null){
				m_GuiConfig.save(m_Gui.getSize(), m_Gui.getExtendedState(), m_Gui.getX(), m_Gui.getY());
				m_GuiConfig = null;
			}
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
