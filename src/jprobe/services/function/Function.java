package jprobe.services.function;

import java.util.Collection;

import util.progress.ProgressListener;
import jprobe.services.data.Data;

/**
 * This interface represents JProbe functions. These functions are run by the user in order to produce new Data
 * objects. Functions define a name, description, and category, which are used to identify and describe the function
 * to the user. The generic type, P, is the parameter class that this function uses to define parameters for
 * function execution. The parameters are set by the Argument objects provided by the getArguments() method. 
 * 
 * @author Tristan Bepler
 *
 * @param <P> - The parameter object used by this function. The parameter object is edited by the Arguments and then
 * passed to the execute method when the function is run.
 */
public interface Function<P> {
	
	/**
	 * The name for this function that will be shown to the user.
	 * @return name
	 */
	public String getName();
	
	/**
	 * A description of this function for the user.
	 * @return description
	 */
	public String getDescription();
	
	/**
	 * A category for this functions. Functions with the same category are grouped together in the GUI.
	 * A value of null or empty string represents no category.
	 * @return category
	 */
	public String getCategory();
	
	/**
	 * Generates an empty parameter object to be filled out by the arguments.
	 * @return parameter object
	 */
	public P newParameters();
	
	/**
	 * The arguments used by this function to fill out the parameter object. Note that if the arguments
	 * have any overlap in their names of short flags, the command line parsing behavior will be
	 * undefined.
	 * @see Argument
	 * @return collection of arguments
	 */
	public Collection<Argument<? super P>> getArguments();
	
	/**
	 * The core functionality of Functions. This method takes a ProgressListener and a parameter object
	 * that has been filled by the Arguments and returns a Data object which is the result of whatever
	 * computations this Function performs.
	 * @param l - ProgressListener to report execution progress to, if progress should be tracked
	 * @param params - parameter object specified by the Arguments
	 * @return data produced by this computation
	 * @see Argument
	 * @see ProgressListener
	 * @see ProgressEvent
	 */
	public Data execute(ProgressListener l, P params) throws Exception;
	
}
