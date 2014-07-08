package util.warning;

/**
 * This interface represents a warning that can be dispatched to query a caller
 * about whether the executing procedure should {@link Reply#PROCEED} or {@link Reply#CANCEL}.
 * A description of the warning is provided by the {@link #getMessage()} method.
 * <P>
 * This calls extends {@link Exception} in order to easily link a {@link Throwable} cause
 * to this Warning and in order to provide {@link #getStackTrace()} and {@link #printStackTrace()}
 * methods if desired. This also means that Warnings should be used sparingly, as frequent Exception
 * construction can be expensive.
 * 
 * @author Tristan Bepler
 *
 */
public class Warning extends Exception{
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructs a new {@link Warning} with the given message, which
	 * can be returned by a call to {@link Warning#getMessage()}. The cause
	 * of this {@link Warning} is set to null.
	 * @param message - description of this {@link Warning}
	 */
	public Warning(String message){
		super(message);
	}
	
	/**
	 * Constructs a new {@link Warning} with the given message and cause.
	 * The message can be returned with a call to {@link Warning#getMessage()} and
	 * the cause can be retrieved with {@link Warning#getCause()}.
	 * @param message - description of this {@link Warning}
	 * @param cause - cause of this {@link Warning}
	 * @see Exception#Exception(String, Throwable)
	 */
	public Warning(String message, Throwable cause) {
		super(message, cause);
	}
	
	
	
}