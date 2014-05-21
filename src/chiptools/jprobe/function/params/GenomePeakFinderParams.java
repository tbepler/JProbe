package chiptools.jprobe.function.params;

import java.io.File;

import chiptools.jprobe.data.Peaks;

public class GenomePeakFinderParams implements GenomeParam, PeaksParam{
	
	private File m_Genome = null;
	private Peaks m_Peaks = null;
	
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

}
