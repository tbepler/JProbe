package plugins.jprobe.gui;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UnsupportedLookAndFeelException;

import jprobe.services.AbstractServiceListener;
import jprobe.services.ErrorHandler;
import jprobe.services.JProbeCore;
import jprobe.services.JProbeCore.Mode;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;

import bepler.crossplatform.Platform;
import plugins.jprobe.gui.services.GUIErrorManager;
import plugins.jprobe.gui.services.JProbeGUI;

public class GUIActivator implements BundleActivator{
	
	private static Bundle m_Bundle = null;
	private static JProbeGUIFrame m_Gui;
	
	private JProbeCore m_Core;
	private GUIConfig m_GuiConfig;
	private AutosaveThread m_AutosaveThread = null;
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
		
		Platform.getInstance().initPlatformSpecificSettings();
		try {
			Platform.getInstance().usePlatformLookAndFeel();
		} catch (UnsupportedLookAndFeelException e) {
			ErrorHandler.getInstance().handleException(e, null);
		}
		
		File prefFile = new File(m_Core.getPreferencesDir() + File.separator + Constants.CONFIG_FILE_NAME);
		m_GuiConfig = new GUIConfig(prefFile);
		
		//start gui
		m_Gui = new JProbeGUIFrame(m_Core, m_Core.getName()+"-"+m_Core.getVersion(), context.getBundle(), m_GuiConfig);
		m_Gui.setVisible(true);
		m_ErrorManager = new GUIErrorManager(m_Gui);
		ErrorHandler.getInstance().addErrorManager(m_ErrorManager);
		m_Registration = context.registerService(JProbeGUI.class, m_Gui, null);
		m_ServiceListeners = initServiceListeners(m_Gui, context);
		
		//check autosave config and start the autosave thread
		if(m_GuiConfig.getAutosave()){
			String autosaveDir = m_Core.getUserDir() + File.separator + Constants.AUTOSAVE_DIR_NAME;
			m_AutosaveThread = new AutosaveThread(m_Core, autosaveDir, m_GuiConfig.getAutosaveFreq(), m_GuiConfig.getMaxAutosaves());
			m_AutosaveThread.start();
		}
		
		//check load workspace config and load the last workspace
		if(m_GuiConfig.getLoadWorkspace()){
			final File last = new File(m_GuiConfig.getLastWorkspace());
			if(last != null && last.exists() && last.canRead()){
				SwingUtilities.invokeLater(new Runnable(){

					@Override
					public void run() {
						SaveLoadUtil.load(m_Core, last);
					}
					
				});
			}
		}
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
		if(m_AutosaveThread != null){
			m_AutosaveThread.terminate();
			m_AutosaveThread = null;
		}
		if(m_Gui != null){
			if(m_GuiConfig != null){
				File lastSave = SaveLoadUtil.getLastSave();
				String path = lastSave == null ? "" : lastSave.getCanonicalPath();
				m_GuiConfig.setLastWorkspace(path);
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
		BackgroundThread.getInstance().terminate();
		BackgroundThread.getInstance().join();
		m_Bundle = null;
	}

}
