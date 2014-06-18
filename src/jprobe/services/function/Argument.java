package jprobe.services.function;

import javax.swing.JComponent;

import util.progress.ProgressListener;

/**
 * This interface represents Arguments for JProbe {@link Function}s. Arguments provide a name, tooltip, and category that are displayed
 * to the user. Arguments also provide a {@link JComponent} that is displayed in the GUI and allows the user to enter a value
 * for this argument. The Argument is further responsible for monitoring its JComponent to report whether the current value is valid
 * and for filling the value into the parameter object when this argument is {@link #process(P)}'d.
 * <p>
 * Arguments are observed by {@link ArgumentListener}s in order to monitor changes in validity.
 * 
 * @author Tristan Bepler
 *
 * @param <P> - the parameter class that this Argument modifies
 */
public interface Argument<P> {
	
	/**
	 * The name for this argument that will be displayed to the user. This also doubles
	 * as the long form flag used to specify this argument on the command line. When used
	 * as a flag on the command line, this value will be preceded by '--'.
	 * @return name
	 */
	public String getName();
	
	/**
	 * A tooltip for this argument that will be shown to the user
	 * @return tooltip
	 */
	public String getTooltip();
	
	/**
	 * A category for this argument. Arguments are grouped by category in the GUI.
	 * A value of null or empty string represents to category.
	 * @return category
	 */
	public String getCategory();
	
	/**
	 * A character that denotes the short flag used to specify this argument on the command line.
	 * A value of null indicates that there is no short flag for this argument. The character will
	 * be preceded by '-' when displayed and used on the command line.
	 * @return short flag character
	 */
	public Character shortFlag();
	
	/**
	 * A prototype value to show for this argument when displaying command line usage to the user.
	 * If no prototype value is desired, then this should return null.
	 * @return prototype value
	 */
	public String getPrototypeValue();
	
	/**
	 * Boolean indicating whether this argument is optional or not (True = optional, False = required).
	 * An optional argument means that the {@link Function} call can proceed without processing this argument.
	 * A non optional argument will always be processed before proceeding with the function call and will
	 * prevent the function from being called if its value is not valid.
	 * @return boolean indicating optional (True) or required (False)
	 * @see #isValid()
	 * @see #process(P)
	 * @see Function
	 * @see Function#execute(ProgressListener, P)
	 */
	public boolean isOptional();
	
	/**
	 * Boolean indicating whether the current value of this argument is valid or not. If the current value
	 * is not valid and this argument is required, then the {@link Function} will not be able to {@link Function#execute(ProgressListener,P)} until
	 * the value of this argument becomes valid. The argument object is responsible for checking its 
	 * {@link JComponent} to see if the value is valid. Furthermore, Arguments should report changes to their
	 * validity to their {@link ArgumentListener}s, if any.
	 * @return boolean indicating whether this argument's value is valid
	 * @see #isOptional()
	 * @see #process(P)
	 * @see #addListener(ArgumentListener)
	 * @see Function
	 */
	public boolean isValid();
	
	/**
	 * Adds an {@link ArgumentListener} to this Argument. ArgumentListeners listen for changes
	 * to this object's validity.
	 * @param l - ArgumentListener to be added
	 * @see #isValid()
	 * @see ArgumentListener#update(Argument, boolean)
	 */
	public void addListener(ArgumentListener l);
	
	/**
	 * Removes the specified {@link ArgumentListener} from this Argument.
	 * @param l - ArgumentListener to remove
	 */
	public void removeListener(ArgumentListener l);
	
	/**
	 * Returns the {@link JComponent} that will be presented to the user in order to fill this argument.
	 * This Argument is responsible for monitoring the JComponent to determine the validity of the value
	 * and to fill the value into the parameter object when this argument is {@link #process(P)}'d.
	 * @return - JComponent used by the user to fill the value of this argument
	 * @see #isValid()
	 * @see #process(P)
	 */
	public JComponent getComponent();
	
	/**
	 * Process this argument and return a parameter object with this argument's value filled in. This method
	 * will only be called if {@link #isValid()} returns True. The resulting parameter object will then be
	 * passed to the {@link Function#execute(ProgressListener, P)} method after all arguments for the {@link Function}
	 * have been processed. This Argument is responsible for monitoring its {@link JComponent} and extracting
	 * the user entered value from the JComponent if applicable.
	 * @param params - parameter object to have this Argument's value added to
	 * @see #isValid()
	 * @see #getComponent()
	 * @see Function
	 * @see Function#execute(ProgressListener, P)
	 */
	public void process(P params);
	
	/**
	 * Parses this argument from the given command line arguments and edits the given parameter object
	 * accordingly. If the args are invalid, then this should throw an exception with an informative message
	 * that will be displayed to the user. After all Arguments are parsed, the parameter will be used
	 * to execute a function.
	 * @param l - a ProgressListener to report parsing progress, if desired
	 * @param params - parameter object to be edited
	 * @param args - command line values for this argument
	 */
	public void parse(ProgressListener l, P params, String[] args);
	
}
