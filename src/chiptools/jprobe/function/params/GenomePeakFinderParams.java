package chiptools.jprobe.function.params;

import java.io.File;

public class GenomePeakFinderParams implements GenomeParam{
	
	private File m_Genome = null;
	
	@Override
	public void setGenomeFile(File f) {
		m_Genome = f;
	}

	@Override
	public File getGenomeFile() {
		return m_Genome;
	}

}
