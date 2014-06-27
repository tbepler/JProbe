package plugins.jprobe.gui;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UnsupportedLookAndFeelException;

import jprobe.services.AbstractServiceListener;
import jprobe.services.DataManager;
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
import plugins.jprobe.gui.services.PluginGUIService;

public class GUIActivator implements BundleActivator{

	private static Bundle m_Bundle = null;

	public static Bundle getBundle(){
		return m_Bundle;
	}
	
	private static void initPlatformSettings(Bundle b){
		Platform.getInstance().initPlatformSpecificSettings();
		try {
			Platform.getInstance().usePlatformLookAndFeel();
		} catch (UnsupportedLookAndFeelException e) {
			ErrorHandler.getInstance().handleException(e, b);
		}
	}
	
	private static JProbeGUIFrame newJProbeFrame(JProbeCore core, DataManager workspace, GUIConfig config){
		return new JProbeGUIFrame(core, core.getName() + "-" + core.getVersion(), config);
	}
	
	private static GUIConfig readConfig(JProbeCore core){
		File prefFile = new File(core.getPreferencesDir() + File.separator + Constants.CONFIG_FILE_NAME);
		return new GUIConfig(prefFile);
	}
	
	private final List<JProbeGUIFrame> m_Frames = new ArrayList<JProbeGUIFrame>();
	private final Collection<AbstractServiceListener<?>> m_ServiceListeners = new ArrayList<AbstractServiceListener<?>>();
	
	private BundleContext m_Context;
	private JProbeCore m_Core;
	private GUIConfig m_GuiConfig;
	private GUIErrorManager m_ErrorManager = null;
	
	private void newGUIServiceListener(JProbeGUIFrame frame){
		AbstractServiceListener<PluginGUIService> listener = new PluginGUIServiceListener(frame, m_Context);
	}
	
	@Override
	public void start(BundleContext context) throws Exception {
		m_Context = context;
		m_Bundle = context.getBundle();
		ServiceReference<JProbeCore> ref = context.getServiceReference(JProbeCore.class);
		m_Core = context.getService(ref);
		if(m_Core.getMode() != Mode.GUI){
			return;
		}
		
		initPlatformSettings(context.getBundle());
		m_GuiConfig = readConfig(m_Core);
		
		//start gui
		JProbeGUIFrame gui = newJProbeFrame(m_Core, m_Core.getDataManager(), m_GuiConfig);
		m_Frames.add(gui);
		gui.setVisible(true);
		
		//start the GUI error manager
		m_ErrorManager = new GUIErrorManager(gui);
		ErrorHandler.getInstance().addErrorManager(m_ErrorManager);
		
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
