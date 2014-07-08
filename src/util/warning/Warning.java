package util.warning;

import java.io.Serializable;

/**
 * This interface represents a warning that can be dispatched to query a caller
 * about whether the executing procedure should {@link Reply#PROCEED} or {@link Reply#CANCEL}.
 * The type of the warning is provided by the {@link #getType()} method.
 * <P>
 * Objects of this class may provide a {@link Throwable} cause accessed through the {@link Warning#getCause()}
 * method if desired.
 * 
 * @author Tristan Bepler
 *
 * @param E - Enumeration containing the possible types of this Warning
 */
public class Warning<E extends Enum<E>> implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private final E m_Type;
	private final Throwable m_Cause;
	
	/**
	 * Constructs a new {@link Warning} with the given message, which
	 * can be returned by a call to {@link Warning#getMessage()}. The cause
	 * of this {@link Warning} is set to null.
	 * @param message - description of this {@link Warning}
	 */
	public Warning(E type){
		this(type, null);
	}
	
	/**
	 * Constructs a new {@link Warning} with the given type and cause.
	 * The type can be returned with a call to {@link Warning#getType()} and
	 * the cause can be retrieved with {@link Warning#getCause()}.
	 * @param message - description of this {@link Warning}
	 * @param cause - cause of this {@link Warning}
	 * @see Exception#Exception(String, Throwable)
	 */
	public Warning(E type, Throwable cause) {
		m_Type = type;
		m_Cause = cause;
	}
	
	/**
	 * Returns the type of this warning.
	 * @return
	 */
	public E getType(){
		return m_Type;
	}
	
	/**
	 * Returns the {@link Throwable} that caused this warning. May be null
	 * if no cause is specified.
	 * @return
	 */
	public Throwable getCause(){
		return m_Cause;
	}
	
	
	
}