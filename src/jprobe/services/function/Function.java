package jprobe.services.function;

import util.progress.ProgressListener;
import jprobe.services.data.Data;
import jprobe.services.data.Field;

public interface Function {
	
	/**
	 * Returns the name of this function.
	 * @return name of this function
	 */
	public String getName();
	
	/**
	 * Returns a description of what this function does.
	 * @return description of this function
	 */
	public String getDescription();
	
	public DataParameter[] getDataParameters();
	public FieldParameter[] getFieldParameters();
	
	/**
	 * This method defines the core behavior of this function. This should never return null. It should throw an exception
	 * instead.
	 * @param listener - a ProgressListener that will receive updates as this function executes
	 * @param dataArgs - the data arguments in the same order as the DataParameter that argument corresponds to
	 * @param fieldArgs - the field arguments in the same order as the FieldParameter that argument corresponds to
	 * @return a Data object that is the result of this function's processing
	 */
	public Data run(ProgressListener listener, Data[] dataArgs, Field[] fieldArgs) throws Exception;
	
}
