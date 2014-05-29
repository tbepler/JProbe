package chiptools.jprobe;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.JComponent;

import jprobe.services.ErrorHandler;
import jprobe.services.JProbeCore;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import chiptools.Constants;
import plugins.jprobe.gui.services.PreferencesTabService;

public class ChiptoolsActivator implements BundleActivator{
	
	public static Bundle getBundle(){
		return BUNDLE;
	}
	
	public static JProbeCore getCore(){
		return CORE;
	}
	
	private static Bundle BUNDLE = null;
	private static JProbeCore CORE = null;
	
	private final Collection<ServiceRegistration<?>> m_ServiceRegs = new ArrayList<ServiceRegistration<?>>();
	
	private FunctionProvider m_FncProvider = new FunctionProvider();
	private ReaderWriterProvider m_RWProvider = new ReaderWriterProvider();
	private PreferencesTabService m_PrefService = new PreferencesTabService(){

		@Override
		public String getTabName() {
			return Constants.PREF_TAB_NAME;
		}

		@Override
		public JComponent getPreferencesTab() {
			return Preferences.getInstance();
		}
		
	};
	
	@Override
	public void start(BundleContext c) throws Exception {
		CORE = c.getService(c.getServiceReference(JProbeCore.class));
		BUNDLE = c.getBundle();
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
		Preferences.getInstance().save(new FileOutputStream(new File(CORE.getPreferencesDir() + File.separator + Constants.PREF_FILE_NAME)));
		m_FncProvider.stop(c);
		m_RWProvider.stop(c);
		for(ServiceRegistration<?> reg : m_ServiceRegs){
			reg.unregister();
		}
		m_ServiceRegs.clear();
		BUNDLE = null;
	}

}
