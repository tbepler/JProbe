package jprobe.services.function;

import jprobe.services.data.Data;
import jprobe.services.data.Field;

public interface FunctionPrototype {
	
	public String getFunctionName();
	public String getFunctionDescription();
	
	public DataParameter[] getDataParameters();
	public FieldParameter[] getFieldParameters();
	
	public Function newInstance(Data[] dataArgs, Field[] fieldArgs) throws InvalidArgumentsException;
	
}
