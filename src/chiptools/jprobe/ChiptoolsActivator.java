package chiptools.jprobe;

import jprobe.services.JProbeCore;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;

import plugins.genome.services.GenomeFunction;
import chiptools.jprobe.data.PeakReaderWriter;
import chiptools.jprobe.data.PeakSequenceReaderWriter;
import chiptools.jprobe.data.PeakSequences;
import chiptools.jprobe.data.Peaks;
import chiptools.jprobe.function.GenomePeakFinder;

public class ChiptoolsActivator implements BundleActivator{
	
	private JProbeCore m_Core = null;
	private PeakReaderWriter m_PeakRW = new PeakReaderWriter();
	private PeakSequenceReaderWriter m_PeakSeqRW = new PeakSequenceReaderWriter();
	private ServiceRegistration<GenomeFunction> m_PeakFinderReg = null;
	
	@Override
	public void start(BundleContext c) throws Exception {
		ServiceReference<JProbeCore> ref = c.getServiceReference(JProbeCore.class);
		m_Core = c.getService(ref);
		m_Core.getDataManager().addDataReader(Peaks.class, m_PeakRW, c.getBundle());
		m_Core.getDataManager().addDataWriter(Peaks.class, m_PeakRW, c.getBundle());
		m_Core.getDataManager().addDataReader(PeakSequences.class, m_PeakSeqRW, c.getBundle());
		m_Core.getDataManager().addDataWriter(PeakSequences.class, m_PeakSeqRW, c.getBundle());
		m_PeakFinderReg = c.registerService(GenomeFunction.class, new GenomePeakFinder(), null);
	}

	@Override
	public void stop(BundleContext c) throws Exception {
		m_Core.getDataManager().removeDataReader(m_PeakRW, c.getBundle());
		m_Core.getDataManager().removeDataWriter(m_PeakRW, c.getBundle());
		m_Core.getDataManager().removeDataReader(m_PeakSeqRW, c.getBundle());
		m_Core.getDataManager().removeDataWriter(m_PeakSeqRW, c.getBundle());
		if(m_PeakFinderReg != null){
			m_PeakFinderReg.unregister();
			m_PeakFinderReg = null;
		}
		m_Core = null;
	}

}
