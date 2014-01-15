package jprobe.services.function;

public interface FunctionPrototype {
	
	public String getFunctionName();
	public String getFunctionDescription();
	
	public DataParameter[] getDataParameters();
	public FieldParameter[] getFieldParameters();
	
	public Function newInstance(DataParameter[] dataParams, FieldParameter[] fieldParams) throws InvalidArgumentsException;
	
}
