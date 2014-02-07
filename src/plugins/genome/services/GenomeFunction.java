package plugins.genome.services;

import jprobe.services.data.Data;
import jprobe.services.function.ProgressListener;

public interface GenomeFunction {
	
	public String getFunctionName();
	
	public Data run(ProgressListener listener);
	
}
