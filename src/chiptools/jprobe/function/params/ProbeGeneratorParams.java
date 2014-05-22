package chiptools.jprobe.function.params;

import chiptools.jprobe.data.Kmer;
import chiptools.jprobe.data.PWM;
import chiptools.jprobe.data.PeakSequences;

public class ProbeGeneratorParams implements PeakSeqsParam, KmerParam, PWMParam, EscoreParam{
	
	public int PROBELEN = 36;
	public int BINDINGSITE = 9;
	public int WINDOWSIZE = 3;
	
	private PeakSequences m_PeakSeqs = null;
	private Kmer m_Kmers = null;
	private PWM m_PWM = null;
	private double m_Escore = 0.4;
	
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

}
