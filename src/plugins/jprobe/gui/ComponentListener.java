package plugins.jprobe.gui;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

import plugins.jprobe.gui.services.ComponentService;
import plugins.jprobe.gui.services.JProbeGUI;
import jprobe.services.AbstractServiceListener;

public class ComponentListener extends AbstractServiceListener<ComponentService>{
	
	private final JProbeGUI m_GUI;
	
	public ComponentListener(JProbeGUI gui, BundleContext context) {
		super(ComponentService.class, context);
		m_GUI = gui;
	}

	@Override
	public void register(ComponentService service, Bundle provider) {
		m_GUI.addComponent(service.getComponent(), service.getGridBagConstraints(), provider);
	}

	@Override
	public void unregister(ComponentService service, Bundle provider) {
		m_GUI.removeComponent(service.getComponent(), provider);
	}

}
