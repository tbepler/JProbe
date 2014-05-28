package chiptools.jprobe.function.probemutator;

import chiptools.jprobe.data.Kmer;
import chiptools.jprobe.data.Probes;
import chiptools.jprobe.function.params.EscoreParam;
import chiptools.jprobe.function.params.KmerParam;
import chiptools.jprobe.function.params.ProbesParam;

public class ProbeMutatorParams implements ProbesParam, KmerParam, EscoreParam{
	
	public int BINDING_SITE_BARRIER = 2;
	
	private double m_Escore = 0.3;
	private Kmer m_Kmer = null;
	private Probes m_Probes = null;
	
	@Override
	public void setEscore(double escore) {
		m_Escore = escore;
	}

	@Override
	public double getEscore() {
		return m_Escore;
	}

	@Override
	public void setKmers(Kmer k) {
		m_Kmer = k;
	}

	@Override
	public Kmer getKmers() {
		return m_Kmer;
	}

	@Override
	public void setProbes(Probes p) {
		m_Probes = p;
	}

	@Override
	public Probes getProbes() {
		return m_Probes;
	}

}
