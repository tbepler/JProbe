package plugins.jprobe.gui;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	
	private static GUIConfig readConfig(JProbeCore core){
		File prefFile = new File(core.getPreferencesDir() + File.separator + Constants.CONFIG_FILE_NAME);
		return new GUIConfig(prefFile);
	}
	
	private BundleContext m_Context;
	private JProbeCore m_Core;
	
	//lazily instantiate data structures, so that they are only instantiated if needed
	private List<JProbeGUIFrame> m_Frames;
	private Map<JProbeGUIFrame, AbstractServiceListener<?>> m_ServiceListeners;
	private GUIConfig m_GuiConfig;
	private GUIErrorManager m_ErrorManager = null;
	
	@Override
	public void start(BundleContext context) throws Exception {
		m_Context = context;
		m_Bundle = context.getBundle();
		ServiceReference<JProbeCore> ref = context.getServiceReference(JProbeCore.class);
		m_Core = context.getService(ref);
		if(m_Core.getMode() != Mode.GUI){
			return;
		}
		
		//initialize data structures lazily
		m_Frames = new ArrayList<JProbeGUIFrame>();
		m_ServiceListeners = new HashMap<JProbeGUIFrame, AbstractServiceListener<?>>();
		
		initPlatformSettings(context.getBundle());
		m_GuiConfig = readConfig(m_Core);
		this.startGUI(m_Core, m_GuiConfig);
		m_ErrorManager = this.startErrorHandler();
	
		
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
		if(m_ErrorManager != null){
			ErrorHandler.getInstance().removeErrorManager(m_ErrorManager);
			m_ErrorManager = null;
		}
		this.stopGUI(m_GuiConfig);
		
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
	

	private void startGUI(JProbeCore core, GUIConfig config){
		JProbeGUIFrame gui = newJProbeFrame(core, core.getDataManager(), config);
		gui.setVisible(true);
	}

	private void stopGUI(GUIConfig config){
		for(JProbeGUIFrame frame : m_Frames){

		}
	}
	
	private GUIErrorManager startErrorHandler(){
		GUIErrorManager err = new GUIErrorManager(m_Frames.get(0));
		ErrorHandler.getInstance().addErrorManager(err);
		return err;
	}
	
	private JProbeGUIFrame newJProbeFrame(JProbeCore core, DataManager workspace, GUIConfig config){
		JProbeGUIFrame frame = new JProbeGUIFrame(core, core.getName() + "-" + core.getVersion(), config);
		m_Frames.add(frame);
		this.newGUIServiceListener(frame);
		return frame;
	}
	
	private void disposeJProbeFrame(JProbeGUIFrame frame){
		this.removeGUIServiceListener(frame);
		m_Frames.remove(frame);
		frame.dispose();
	}
	
	private AbstractServiceListener<PluginGUIService> newGUIServiceListener(JProbeGUIFrame frame){
		AbstractServiceListener<PluginGUIService> listener = new PluginGUIServiceListener(frame, m_Context);
		m_ServiceListeners.put(frame, listener);
		listener.load();
		return listener;
	}
	
	private void removeGUIServiceListener(JProbeGUIFrame frame){
		if(m_ServiceListeners.containsKey(frame)){
			AbstractServiceListener<?> listener = m_ServiceListeners.get(frame);
			listener.unload();
			listener.unregisterAll();
			m_ServiceListeners.remove(frame);
		}
	}

}
