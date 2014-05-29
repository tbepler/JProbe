package plugins.jprobe.gui;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

import plugins.jprobe.gui.services.JProbeGUI;
import plugins.jprobe.gui.services.PreferencesTabService;
import jprobe.services.AbstractServiceListener;

public class PreferencesTabListener extends AbstractServiceListener<PreferencesTabService>{
	
	private final JProbeGUI m_GUI;
	
	public PreferencesTabListener(JProbeGUI gui, BundleContext context) {
		super(PreferencesTabService.class, context);
		m_GUI = gui;
	}

	@Override
	public void register(PreferencesTabService service, Bundle provider) {
		m_GUI.addPreferencesTab(service.getPreferencesTab(), service.getTabName(), provider);
	}

	@Override
	public void unregister(PreferencesTabService service, Bundle provider) {
		m_GUI.removePreferencesTab(service.getPreferencesTab(), provider);
	}

}
