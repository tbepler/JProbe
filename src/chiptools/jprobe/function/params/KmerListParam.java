package chiptools.jprobe.function.params;

import java.util.List;

import chiptools.jprobe.data.Kmer;

public interface KmerListParam {
	
	public void setKmers(List<Kmer> kmers);
	public List<Kmer> getKmers();
	
}
