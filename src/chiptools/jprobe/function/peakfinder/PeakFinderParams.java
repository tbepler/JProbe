package chiptools.jprobe.function.peakfinder;

import java.io.File;

import chiptools.jprobe.data.Peaks;
import chiptools.jprobe.function.params.GenomeParam;
import chiptools.jprobe.function.params.PeaksParam;
import chiptools.jprobe.function.params.SummitParam;

public class PeakFinderParams implements GenomeParam, PeaksParam, SummitParam{
	
	private File m_Genome = null;
	private Peaks m_Peaks = null;
	private int m_Summit = -1;
	
	@Override
	public void setGenomeFile(File f) {
		m_Genome = f;
	}

	@Override
	public File getGenomeFile() {
		return m_Genome;
	}

	@Override
	public void setPeaks(Peaks p) {
		m_Peaks = p;
	}

	@Override
	public Peaks getPeaks() {
		return m_Peaks;
	}

	@Override
	public void setSummit(int summit) {
		m_Summit = summit;
	}

	@Override
	public int getSummit() {
		return m_Summit;
	}

}
