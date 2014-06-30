package jprobe.services.data;

public class WriteException extends Exception{
	private static final long serialVersionUID = 1L;

	public WriteException() {
		super();
	}

	public WriteException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public WriteException(String message, Throwable cause) {
		super(message, cause);
	}

	public WriteException(String message) {
		super(message);
	}

	public WriteException(Throwable cause) {
		super(cause);
	}

}
