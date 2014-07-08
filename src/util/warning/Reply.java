package util.warning;

/**
 * Enumeration representing the responses a {@link WarningHandler} can return
 * when alerted with a {@link Warning}.
 * 
 * @author Tristan Bepler
 *
 */
public enum Reply{
	
	/**
	 * Informs the {@link Warning} producer that it should proceed with
	 * execution.
	 */
	PROCEED,
	
	/**
	 * Informs the {@link Warning} producer that it should cancel execution.
	 */
	CANCEL;
}