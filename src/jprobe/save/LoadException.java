package jprobe.save;

public class LoadException extends Exception{
	public LoadException() {
		super();
	}

	public LoadException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public LoadException(String message, Throwable cause) {
		super(message, cause);
	}

	public LoadException(String message) {
		super(message);
	}

	public LoadException(Throwable cause) {
		super(cause);
	}

	private static final long serialVersionUID = 1L;

}
