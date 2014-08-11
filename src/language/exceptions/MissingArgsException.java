package language.exceptions;

public class MissingArgsException extends RuntimeException{
	private static final long serialVersionUID = 1L;

	public MissingArgsException() {
		super();
	}

	public MissingArgsException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public MissingArgsException(String message, Throwable cause) {
		super(message, cause);
	}

	public MissingArgsException(String message) {
		super(message);
	}

	public MissingArgsException(Throwable cause) {
		super(cause);
	}
	
	

}
