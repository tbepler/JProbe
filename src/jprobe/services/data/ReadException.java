package jprobe.services.data;

public class ReadException extends Exception{
	private static final long serialVersionUID = 1L;

	public ReadException() {
		super();
	}

	public ReadException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ReadException(String message, Throwable cause) {
		super(message, cause);
	}

	public ReadException(String message) {
		super(message);
	}

	public ReadException(Throwable cause) {
		super(cause);
	}

}
