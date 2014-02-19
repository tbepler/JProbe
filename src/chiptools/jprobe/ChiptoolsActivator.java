package chiptools.jprobe;

import jprobe.services.JProbeCore;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import chiptools.jprobe.data.PeakReaderWriter;
import chiptools.jprobe.data.Peaks;

public class ChiptoolsActivator implements BundleActivator{
	
	private JProbeCore m_Core = null;
	private PeakReaderWriter m_PeakRW = new PeakReaderWriter();
	
	@Override
	public void start(BundleContext c) throws Exception {
		ServiceReference<JProbeCore> ref = c.getServiceReference(JProbeCore.class);
		m_Core = c.getService(ref);
		m_Core.getDataManager().addDataReader(Peaks.class, m_PeakRW, c.getBundle());
		m_Core.getDataManager().addDataWriter(Peaks.class, m_PeakRW, c.getBundle());
	}

	@Override
	public void stop(BundleContext c) throws Exception {
		m_Core.getDataManager().removeDataReader(m_PeakRW, c.getBundle());
		m_Core.getDataManager().removeDataWriter(m_PeakRW, c.getBundle());
		m_Core = null;
	}

}
