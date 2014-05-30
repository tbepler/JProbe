package chiptools.jprobe;

import java.awt.Frame;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collection;

import jprobe.services.ErrorHandler;
import jprobe.services.JProbeCore;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;

import chiptools.Constants;
import plugins.jprobe.gui.services.JProbeGUI;
import plugins.jprobe.gui.services.PreferencesPanel;
import plugins.jprobe.gui.services.PreferencesTabService;

public class ChiptoolsActivator implements BundleActivator{
	
	public static Bundle getBundle(){
		return BUNDLE;
	}
	
	public static JProbeCore getCore(){
		return CORE;
	}
	
	public static Frame getGUIFrame(){
		return GUI != null ? GUI.getGUIFrame() : null;
	}
	
	private static Bundle BUNDLE = null;
	private static JProbeCore CORE = null;
	private static JProbeGUI GUI = null;
	
	private final Collection<ServiceRegistration<?>> m_ServiceRegs = new ArrayList<ServiceRegistration<?>>();
	
	private FunctionProvider m_FncProvider = new FunctionProvider();
	private ReaderWriterProvider m_RWProvider = new ReaderWriterProvider();
	private PreferencesTabService m_PrefService = new PreferencesTabService(){

		@Override
		public String getTabName() {
			return Constants.PREF_TAB_NAME;
		}

		@Override
		public PreferencesPanel getPreferencesTab() {
			return Preferences.getInstance();
		}
		
	};
	
	private BundleContext m_BC;
	
	private ServiceListener sl = new ServiceListener() {
		@Override
		public void serviceChanged(ServiceEvent ev) {
			ServiceReference<?> sr = ev.getServiceReference();
			switch(ev.getType()) {
				case ServiceEvent.REGISTERED:
					GUI = (JProbeGUI) m_BC.getService(sr);
					break;
				default:
					break;
			}
		}
	};
	
	@Override
	public void start(BundleContext c) throws Exception {
		m_BC = c;
		CORE = c.getService(c.getServiceReference(JProbeCore.class));
		BUNDLE = c.getBundle();
		
		String filter = "(objectclass="+JProbeGUI.class.getName()+")";
		c.addServiceListener(sl, filter);
		Collection<ServiceReference<JProbeGUI>> refs = c.getServiceReferences(JProbeGUI.class, null);
		for(ServiceReference<?> r : refs){
			sl.serviceChanged(new ServiceEvent(ServiceEvent.REGISTERED, r));
		}
		
		try{
			Preferences.getInstance().load(new FileInputStream(new File(CORE.getPreferencesDir() + File.separator + Constants.PREF_FILE_NAME)));
		}catch(FileNotFoundException e){
			ErrorHandler.getInstance().handleException(e, BUNDLE);
		}
		m_FncProvider.start(c);
		m_RWProvider.start(c);
		m_ServiceRegs.add(c.registerService(PreferencesTabService.class, m_PrefService, null));
	}

	@Override
	public void stop(BundleContext c) throws Exception {
		try{
			Preferences.getInstance().save(new FileOutputStream(new File(CORE.getPreferencesDir() + File.separator + Constants.PREF_FILE_NAME)));
		} catch (FileNotFoundException e){
			File f = new File(CORE.getPreferencesDir() + File.separator + Constants.PREF_FILE_NAME);
			f.getParentFile().mkdirs();
			Preferences.getInstance().save(new FileOutputStream(f));
		}
		m_FncProvider.stop(c);
		m_RWProvider.stop(c);
		for(ServiceRegistration<?> reg : m_ServiceRegs){
			reg.unregister();
		}
		m_ServiceRegs.clear();
		BUNDLE = null;
		CORE = null;
		GUI = null;
	}

}
