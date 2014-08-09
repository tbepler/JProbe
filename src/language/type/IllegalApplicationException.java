package language.type;

public class IllegalApplicationException extends Exception{
	private static final long serialVersionUID = 1L;

	public IllegalApplicationException() {
		super();
	}

	public IllegalApplicationException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public IllegalApplicationException(String message, Throwable cause) {
		super(message, cause);
	}

	public IllegalApplicationException(String message) {
		super(message);
	}

	public IllegalApplicationException(Throwable cause) {
		super(cause);
	}
	
	
	
}
