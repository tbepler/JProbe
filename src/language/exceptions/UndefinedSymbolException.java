package language.exceptions;

public class UndefinedSymbolException extends Exception{
	private static final long serialVersionUID = 1L;

	public UndefinedSymbolException() {
		super();
	}

	public UndefinedSymbolException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public UndefinedSymbolException(String message, Throwable cause) {
		super(message, cause);
	}

	public UndefinedSymbolException(String message) {
		super(message);
	}

	public UndefinedSymbolException(Throwable cause) {
		super(cause);
	}
	
	

}
