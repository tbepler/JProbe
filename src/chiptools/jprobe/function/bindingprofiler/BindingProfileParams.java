package chiptools.jprobe.function.bindingprofiler;

import java.util.ArrayList;
import java.util.List;

import chiptools.jprobe.data.Kmer;
import chiptools.jprobe.data.PWM;
import chiptools.jprobe.data.Probes;
import chiptools.jprobe.function.params.KmerListParam;
import chiptools.jprobe.function.params.PWMListParam;
import chiptools.jprobe.function.params.ProbesParam;

public class BindingProfileParams implements ProbesParam, KmerListParam, PWMListParam{
	
	public final List<String> KMER_NAMES = new ArrayList<String>();
	public final List<String> PWM_NAMES = new ArrayList<String>();
	
	private Probes m_Probes = null;
	private List<Kmer> m_Kmers = new ArrayList<Kmer>();
	private List<PWM> m_PWMs = new ArrayList<PWM>();
	
	@Override
	public void setProbes(Probes p) {
		m_Probes = p;
	}

	@Override
	public Probes getProbes() {
		return m_Probes;
	}

	@Override
	public void setPWMs(List<PWM> pwms) {
		m_PWMs = pwms;
	}

	@Override
	public List<PWM> getPWMs() {
		return m_PWMs;
	}

	@Override
	public void setKmers(List<Kmer> kmers) {
		m_Kmers = kmers;
	}

	@Override
	public List<Kmer> getKmers() {
		return m_Kmers;
	}

}
