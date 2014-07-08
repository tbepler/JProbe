package util.warning;

/**
 * This interface defines the {@link #handle(Warning)} method which can be
 * used by an executing procedure to alert the caller of an {@link Warning},
 * which allows the caller to specify if the procedure should {@link Reply#PROCEED}
 * or {@link Reply#CANCEL}.
 * 
 * @author Tristan Bepler
 * 
 * @param E - Enumeration containing the types of {@link Warning}s this WarningHandler
 * can receive.
 *
 */
public interface WarningHandler<E extends Enum<E>> {
	
	/**
	 * Alerts this {@link WarningHandler} that the given {@link Warning} has occurred.
	 * The {@link Warning#getType()} method can be used to retrieve the type of the Warning.
	 * The {@link Warning#getCause()} method can be used to retrieve the {@link Throwable}
	 * cause of this Warning, if there is one.
	 * @param warning - Warning object being reported.
	 * @return a {@link Reply} indicating whether the Warning producer should
	 * {@link Reply#PROCEED} with or {@link Reply#CANCEL} execution.
	 */
	public Reply handle(Warning<E> warning);
	
}
