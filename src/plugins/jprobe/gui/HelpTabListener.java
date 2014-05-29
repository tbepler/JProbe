package plugins.jprobe.gui;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

import plugins.jprobe.gui.services.HelpTabService;
import plugins.jprobe.gui.services.JProbeGUI;
import jprobe.services.AbstractServiceListener;

public class HelpTabListener extends AbstractServiceListener<HelpTabService>{
	
	private final JProbeGUI m_GUI;
	
	public HelpTabListener(JProbeGUI gui, BundleContext context) {
		super(HelpTabService.class, context);
		m_GUI = gui;
	}

	@Override
	public void register(HelpTabService service, Bundle provider) {
		m_GUI.addHelpTab(service.getHelpTab(), service.getTabName(), provider);
	}

	@Override
	public void unregister(HelpTabService service, Bundle provider) {
		m_GUI.removeHelpTab(service.getHelpTab(), provider);
	}

}
