package chiptools.jprobe.function.probegenerator;

import chiptools.jprobe.data.Kmer;
import chiptools.jprobe.data.PWM;
import chiptools.jprobe.data.PeakSequences;
import chiptools.jprobe.function.params.EscoreParam;
import chiptools.jprobe.function.params.KmerParam;
import chiptools.jprobe.function.params.PWMParam;
import chiptools.jprobe.function.params.PeakSeqsParam;
import chiptools.jprobe.function.params.ProbeLenParam;

public class ProbeGeneratorParams implements PeakSeqsParam, KmerParam, PWMParam, EscoreParam, ProbeLenParam{
	
	public int BINDINGSITE = 9;
	public int WINDOWSIZE = 3;
	
	private PeakSequences m_PeakSeqs = null;
	private Kmer m_Kmers = null;
	private PWM m_PWM = null;
	private double m_Escore = 0.4;
	private int m_ProbeLen = 36;
	
	@Override
	public void setKmers(Kmer k) {
		m_Kmers = k;
	}

	@Override
	public Kmer getKmers() {
		return m_Kmers;
	}

	@Override
	public void setPeakSeqs(PeakSequences peakSeqs) {
		m_PeakSeqs = peakSeqs;
	}

	@Override
	public PeakSequences getPeakSeqs() {
		return m_PeakSeqs;
	}

	@Override
	public void setPWM(PWM p) {
		m_PWM = p;
	}

	@Override
	public PWM getPWM() {
		return m_PWM;
	}

	@Override
	public void setEscore(double escore) {
		m_Escore = escore;
	}

	@Override
	public double getEscore() {
		return m_Escore;
	}

	@Override
	public void setProbeLength(int length) {
		m_ProbeLen = length;
	}

	@Override
	public int getProbeLength() {
		return m_ProbeLen;
	}

}
