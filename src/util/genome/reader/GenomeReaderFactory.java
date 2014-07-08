package util.genome.reader;

import java.io.File;

public class GenomeReaderFactory {
	
	public static GenomeReader createGenomeReader(File genomeFile){
		return new BasicGenomeReader(genomeFile);
	}
	
}
