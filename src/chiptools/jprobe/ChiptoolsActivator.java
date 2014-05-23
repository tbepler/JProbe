package chiptools.jprobe;

import jprobe.services.JProbeCore;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class ChiptoolsActivator implements BundleActivator{
	
	public static Bundle getBundle(){
		return BUNDLE;
	}
	
	public static JProbeCore getCore(){
		return CORE;
	}
	
	private static Bundle BUNDLE = null;
	private static JProbeCore CORE = null;
	
	private FunctionProvider m_FncProvider = new FunctionProvider();
	private ReaderWriterProvider m_RWProvider = new ReaderWriterProvider();
	
	@Override
	public void start(BundleContext c) throws Exception {
		CORE = c.getService(c.getServiceReference(JProbeCore.class));
		BUNDLE = c.getBundle();
		m_FncProvider.start(c);
		m_RWProvider.start(c);
	}

	@Override
	public void stop(BundleContext c) throws Exception {
		m_FncProvider.stop(c);
		m_RWProvider.stop(c);
		BUNDLE = null;
	}

}
