package jprobe.services;


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
	public Class<? extends Data>[] getRequiredArgs();
	
	/**
	 * This method should be defined to return an array of classes, where each element of the array represents an optional
	 * DataType object that should be passed to this module when it is run.
	 * @return
	 */
	public Class<? extends Data>[] getOptionalArgs();
	
	/**
	 * This method defines the core behavior of this function. It takes two Data arrays, one specifying the required
	 * Data arguments and one specifying the optional Data arguments. The class of each Data object will
	 * match the class specified for that index by the getRequiredArgs or getOptionalArgs methods. For the optional args, 
	 * args that are not specified will have a value of null. This should never return null. It should throw an exception
	 * instead.
	 * @param required - Data array specifying the required Data arguments
	 * @param optional - Data array specifying the optional Data arguments, elements of null mean none specified.
	 * @return a Data object that is the result of this function's processing
	 */
	public Data run(Data[] required, Data[] optional) throws Exception;
}
