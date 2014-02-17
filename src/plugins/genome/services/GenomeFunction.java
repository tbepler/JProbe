package plugins.genome.services;

import util.genome.reader.GenomeReader;
import util.progress.ProgressListener;
import jprobe.services.data.Data;
import jprobe.services.data.Field;
import jprobe.services.function.DataParameter;
import jprobe.services.function.FieldParameter;
import jprobe.services.function.InvalidArgumentsException;

public interface GenomeFunction {
	
	public String getName();
	public String getDescription();
	
	public DataParameter[] getDataParameters();
	public FieldParameter[] getFieldParameters();
	
	public Data run(GenomeReader genomeReader, Data[] dataArgs, Field[] fieldArgs) throws Exception;
	
}
