package chiptools.jprobe;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class ChiptoolsActivator implements BundleActivator{
	
	public static Bundle getBundle(){
		return BUNDLE;
	}
	
	private static Bundle BUNDLE = null;
	
	private CommandProvider m_CmdProvider = new CommandProvider();
	private FunctionProvider m_FncProvider = new FunctionProvider();
	private ReaderWriterProvider m_RWProvider = new ReaderWriterProvider();
	
	@Override
	public void start(BundleContext c) throws Exception {
		BUNDLE = c.getBundle();
		m_FncProvider.start(c);
		m_CmdProvider.start(c);
		m_RWProvider.start(c);
	}

	@Override
	public void stop(BundleContext c) throws Exception {
		m_FncProvider.stop(c);
		m_CmdProvider.stop(c);
		m_RWProvider.stop(c);
		BUNDLE = null;
	}

}
