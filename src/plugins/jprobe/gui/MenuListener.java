package plugins.jprobe.gui;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

import plugins.jprobe.gui.services.JProbeGUI;
import plugins.jprobe.gui.services.MenuService;
import jprobe.services.AbstractServiceListener;

public class MenuListener extends AbstractServiceListener<MenuService>{
	
	private final JProbeGUI m_GUI;
	
	public MenuListener(JProbeGUI gui, BundleContext context) {
		super(MenuService.class, context);
		m_GUI = gui;
	}

	@Override
	public void register(MenuService service, Bundle provider) {
		m_GUI.addDropdownMenu(service.getMenu(), provider);
	}

	@Override
	public void unregister(MenuService service, Bundle provider) {
		m_GUI.removeDropdownMenu(service.getMenu(), provider);
	}

}
