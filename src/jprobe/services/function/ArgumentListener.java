package jprobe.services.function;

/**
 * This interface defines {@link Argument} observers. It declares one method, {@link #update(Argument, boolean)},
 * which is called by observed Arguments to report changes in their validity.
 * 
 * @author Tristan Bepler
 * 
 * @see Argument
 * @see Argument#isValid()
 *
 */
public interface ArgumentListener {
	
	/**
	 * This method is used to report changes in the validity of observed {@link Argument}s.
	 * It is called by those Arguments.
	 * @param arg - Argument object reporting changes
	 * @param valid - new validity state
	 * @see Argument
	 * @see Argument#isValid()
	 */
	public void update(Argument<?> arg, boolean valid);
	
}
