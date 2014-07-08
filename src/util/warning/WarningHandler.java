package util.warning;

/**
 * This interface defines the {@link #handle(Warning)} method which can be
 * used by an executing procedure to alert the caller of an {@link Warning},
 * which allows the caller to specify if the procedure should {@link Warning#proceed()}
 * or {@link Warning#cancel()}.
 * 
 * @author Tristan Bepler
 *
 */
public interface WarningHandler {
	
	/**
	 * Alerts this {@link WarningHandler} that the given {@link Warning} has occurred.
	 * The {@link Warning#getMessage()} method can be used to retrieve a description
	 * of the warning.
	 * @param warning - {@link Warning} object being reported.
	 * @return a {@link Reply} indicating whether the {@link Warning} producer should
	 * {@link Reply#PROCEED} with or {@link Reply#CANCEL} execution.
	 */
	public Reply handle(Warning warning);
	
}
