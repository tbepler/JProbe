package old.modules;

import old.core.exceptions.ModuleExecutionException;
import old.datatypes.DataType;

public interface Module{
	
	/**
	 * This method should be defined to return an array of classes, where each element of the array represents a required
	 * DataType object that should be passed to this module when it is run.
	 * @return a class array
	 */
	public Class<? extends DataType>[] getRequiredArgs();
	
	/**
	 * This method should be defined to return an array of classes, where each element of the array represents an optional
	 * DataType object that should be passed to this module when it is run.
	 * @return
	 */
	public Class<? extends DataType>[] getOptionalArgs();
	
	/**
	 * This method defines the core behavior of this module. It takes two DataType arrays, one specifying the required
	 * DataType arguments and one specifying the optional DataType arguments. The class of each of these DataTypes will
	 * match the class specified for that index by the getRequiredArgs or getOptionalArgs methods. For the optional args, 
	 * args that are not specified will have a value of null. This should never return null. It should throw an exception
	 * instead.
	 * @param required - DataType array specifying the required DataType arguments
	 * @param optional - DataType array specifying the optional DataType arguments, elements of null mean no specified.
	 * @return a DataType object that is the result of this module's processing
	 */
	public DataType run(DataType[] required, DataType[] optional) throws ModuleExecutionException;
	
	
	
}
