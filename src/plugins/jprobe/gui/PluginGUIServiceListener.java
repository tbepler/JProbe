package plugins.jprobe.gui;

import java.util.HashMap;
import java.util.Map;

import javax.swing.SwingUtilities;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

import plugins.jprobe.gui.services.PluginGUIService;
import jprobe.services.AbstractServiceListener;

public class PluginGUIServiceListener extends AbstractServiceListener<PluginGUIService>{
	
	private final Map<PluginGUIService, PluginServiceView> m_ServiceViews = new HashMap<PluginGUIService, PluginServiceView>();
	private final JProbeGUIFrame m_Frame;
	
	public PluginGUIServiceListener(JProbeGUIFrame frame, BundleContext context) {
		super(PluginGUIService.class, context);
		m_Frame = frame;
	}

	@Override
	public void register(final PluginGUIService service, Bundle provider) {
		final PluginServiceView view;
		if(!m_ServiceViews.containsKey(provider)){ //expected case
			view = new PluginServiceView(m_Frame, provider);
			m_ServiceViews.put(service, view);
		}else{
			//this means the service is being reregistered...
			view = m_ServiceViews.get(provider);
		}
		SwingUtilities.invokeLater(new Runnable(){

			@Override
			public void run() {
				service.createComponents(view);
			}
			
		});
	}

	@Override
	public void unregister(PluginGUIService service, Bundle provider) {
		if(m_ServiceViews.containsKey(service)){
			final PluginServiceView view = m_ServiceViews.get(service);
			SwingUtilities.invokeLater(new Runnable(){

				@Override
				public void run() {
					view.removeAll();
				}
				
			});
		}
	}

}
