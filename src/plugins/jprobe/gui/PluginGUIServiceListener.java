package plugins.jprobe.gui;

import java.util.HashMap;
import java.util.Map;

import javax.swing.SwingUtilities;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

import plugins.jprobe.gui.services.PluginGUIService;
import jprobe.services.AbstractServiceListener;

public class PluginGUIServiceListener extends AbstractServiceListener<PluginGUIService>{
	
	private final Map<Bundle, PluginServiceView> m_BundleViews = new HashMap<Bundle, PluginServiceView>();
	private final JProbeGUIFrame m_Frame;
	
	public PluginGUIServiceListener(JProbeGUIFrame frame, BundleContext context) {
		super(PluginGUIService.class, context);
		m_Frame = frame;
	}

	@Override
	public void register(final PluginGUIService service, Bundle provider) {
		final PluginServiceView view;
		if(!m_BundleViews.containsKey(provider)){ //expected case
			view = new PluginServiceView(m_Frame, provider);
			m_BundleViews.put(provider, view);
		}else{
			//this means the service is being reregistered...
			view = m_BundleViews.get(provider);
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
		if(m_BundleViews.containsKey(provider)){
			final PluginServiceView view = m_BundleViews.get(provider);
			SwingUtilities.invokeLater(new Runnable(){

				@Override
				public void run() {
					view.removeAll();
				}
				
			});
		}
	}

}
