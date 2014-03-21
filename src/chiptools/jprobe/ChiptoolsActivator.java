package chiptools.jprobe;

import jprobe.services.JProbeCore;
import jprobe.services.function.FunctionPrototype;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;

import plugins.genome.services.GenomeFunction;
import chiptools.jprobe.data.Kmer;
import chiptools.jprobe.data.KmerReaderWriter;
import chiptools.jprobe.data.PWM;
import chiptools.jprobe.data.PWMReaderWriter;
import chiptools.jprobe.data.PeakReaderWriter;
import chiptools.jprobe.data.PeakSequenceReaderWriter;
import chiptools.jprobe.data.PeakSequences;
import chiptools.jprobe.data.Peaks;
import chiptools.jprobe.data.Probes;
import chiptools.jprobe.data.ProbesReaderWriter;
import chiptools.jprobe.function.GenomePeakFinder;
import chiptools.jprobe.function.ProbeGeneratorPrototype;

public class ChiptoolsActivator implements BundleActivator{
	
	public static Bundle getBundle(){
		return BUNDLE;
	}
	
	private static Bundle BUNDLE = null;
	
	private JProbeCore m_Core = null;
	private ProbesReaderWriter m_ProbeRW = new ProbesReaderWriter();
	private PWMReaderWriter m_pwmRW = new PWMReaderWriter();
	private KmerReaderWriter m_KmerRW = new KmerReaderWriter();
	private PeakReaderWriter m_PeakRW = new PeakReaderWriter();
	private PeakSequenceReaderWriter m_PeakSeqRW = new PeakSequenceReaderWriter();
	private FunctionPrototype m_ProbeGenPrototyp = new ProbeGeneratorPrototype();
	private ServiceRegistration<GenomeFunction> m_PeakFinderReg = null;
	private CommandProvider m_CmdProvider = new CommandProvider();
	
	@Override
	public void start(BundleContext c) throws Exception {
		BUNDLE = c.getBundle();
		ServiceReference<JProbeCore> ref = c.getServiceReference(JProbeCore.class);
		m_Core = c.getService(ref);
		m_Core.getDataManager().addDataReader(Peaks.class, m_PeakRW, c.getBundle());
		m_Core.getDataManager().addDataWriter(Peaks.class, m_PeakRW, c.getBundle());
		m_Core.getDataManager().addDataReader(PeakSequences.class, m_PeakSeqRW, c.getBundle());
		m_Core.getDataManager().addDataWriter(PeakSequences.class, m_PeakSeqRW, c.getBundle());
		m_Core.getDataManager().addDataReader(Kmer.class, m_KmerRW, c.getBundle());
		m_Core.getDataManager().addDataWriter(Kmer.class, m_KmerRW, c.getBundle());
		m_Core.getDataManager().addDataReader(PWM.class, m_pwmRW, c.getBundle());
		m_Core.getDataManager().addDataWriter(PWM.class, m_pwmRW, c.getBundle());
		m_Core.getDataManager().addDataReader(Probes.class, m_ProbeRW, c.getBundle());
		m_Core.getDataManager().addDataWriter(Probes.class, m_ProbeRW, c.getBundle());
		m_Core.getFunctionManager().addFunctionPrototype(m_ProbeGenPrototyp, c.getBundle());
		m_PeakFinderReg = c.registerService(GenomeFunction.class, new GenomePeakFinder(), null);
		m_CmdProvider.start(c);
	}

	@Override
	public void stop(BundleContext c) throws Exception {
		if(m_Core != null){
			m_Core.getDataManager().removeDataReader(m_PeakRW, c.getBundle());
			m_Core.getDataManager().removeDataWriter(m_PeakRW, c.getBundle());
			m_Core.getDataManager().removeDataReader(m_PeakSeqRW, c.getBundle());
			m_Core.getDataManager().removeDataWriter(m_PeakSeqRW, c.getBundle());
			m_Core.getDataManager().removeDataReader(m_KmerRW, c.getBundle());
			m_Core.getDataManager().removeDataWriter(m_KmerRW, c.getBundle());
			m_Core.getDataManager().removeDataReader(m_pwmRW, c.getBundle());
			m_Core.getDataManager().removeDataWriter(m_pwmRW, c.getBundle());
			m_Core.getDataManager().removeDataReader(m_ProbeRW, c.getBundle());
			m_Core.getDataManager().removeDataWriter(m_ProbeRW, c.getBundle());
			m_Core.getFunctionManager().removeFunctionPrototype(m_ProbeGenPrototyp, c.getBundle());
			m_Core = null;
		}
		if(m_PeakFinderReg != null){
			m_PeakFinderReg.unregister();
			m_PeakFinderReg = null;
		}
		m_CmdProvider.stop(c);
		BUNDLE = null;
	}

}
