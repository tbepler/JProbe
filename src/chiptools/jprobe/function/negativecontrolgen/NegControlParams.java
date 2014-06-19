package chiptools.jprobe.function.negativecontrolgen;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import chiptools.jprobe.data.Kmer;
import chiptools.jprobe.data.Peaks;
import chiptools.jprobe.function.params.EscoreParam;
import chiptools.jprobe.function.params.GenomeParam;
import chiptools.jprobe.function.params.KmerListParam;
import chiptools.jprobe.function.params.ProbeLenParam;
import chiptools.jprobe.function.params.SummitParam;

public class NegControlParams implements GenomeParam, SummitParam, KmerListParam, EscoreParam, ProbeLenParam{
	
	private List<Peaks> m_Include = new ArrayList<Peaks>();
	private List<Peaks> m_Exclude = new ArrayList<Peaks>();
	private File m_Genome = null;
	private int m_Summit = -1;
	private List<Kmer> m_Kmers = new ArrayList<Kmer>();
	private double m_Escore = 0.3;
	private int m_Num = -1;
	private int m_Len = 36;
	
	public void setExcludePeaks(List<Peaks> exclude){
		m_Exclude = exclude;
	}
	
	public List<Peaks> getExcludePeaks(){
		return m_Exclude;
	}
	
	public void setIncludePeaks(List<Peaks> include){
		m_Include = include;
	}
	
	public List<Peaks> getIncludePeaks(){
		return m_Include;
	}
	
	@Override
	public void setGenomeFile(File f) {
		m_Genome = f;
	}

	@Override
	public File getGenomeFile() {
		return m_Genome;
	}

	@Override
	public void setSummit(int summit) {
		m_Summit = summit;
	}

	@Override
	public int getSummit() {
		return m_Summit;
	}

	@Override
	public void setKmers(List<Kmer> kmers) {
		m_Kmers = kmers;
	}

	@Override
	public List<Kmer> getKmers() {
		return m_Kmers;
	}

	@Override
	public void setEscore(double escore) {
		m_Escore = escore;
	}

	@Override
	public double getEscore() {
		return m_Escore;
	}
	
	public void setNumPeaks(int num){
		m_Num = num;
	}
	
	public int getNumPeaks(){
		return m_Num;
	}

	@Override
	public void setProbeLength(int length) {
		m_Len = length;
	}

	@Override
	public int getProbeLength() {
		return m_Len;
	}

}
