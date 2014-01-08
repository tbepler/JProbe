package jprobe.services.function;

import java.util.List;

import jprobe.services.data.Data;
import jprobe.services.data.DataField;



public interface Function {
	
	/**
	 * Returns the name of this function.
	 * @return name of this function
	 */
	public String getName();
	
	/**
	 * Returns a description of what this function does.
	 * @return despcription of this function
	 */
	public String getDescription();
	
	/**
	 * This method should be defined to return an array of classes, where each element of the array represents a required
	 * DataType object that should be passed to this module when it is run.
	 * @return a class array
	 */
	public List<Class<? extends Data>> getRequiredDataArgs();
	
	/**
	 * This method should be defined to return an array of classes, where each element of the array represents an optional
	 * DataType object that should be passed to this module when it is run.
	 * @return
	 */
	public List<Class<? extends Data>> getOptionalDataArgs();
	
	public List<DataField> getRequiredFields();
	
	public List<DataField> getOptionalFields();
	
	public boolean isProgressTrackable();
	public int getProgressLength(FunctionParam params);
	public void addListener(FunctionListener listener);
	public void removeListener(FunctionListener listener);
	
	/**
	 * This method defines the core behavior of this function. It takes two Data arrays, one specifying the required
	 * Data arguments and one specifying the optional Data arguments. The class of each Data object will
	 * match the class specified for that index by the getRequiredDataArgs or getOptionalDataArgs methods. For the optional args, 
	 * args that are not specified will have a value of null. This should never return null. It should throw an exception
	 * instead.
	 * @param params - A FunctionParam object containing the arguments for this function
	 * @return a Data object that is the result of this function's processing
	 */
	public Data run(FunctionParam params) throws Exception;
}
