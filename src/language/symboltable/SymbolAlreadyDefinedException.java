package language.symboltable;

public class SymbolAlreadyDefinedException extends Exception{
	private static final long serialVersionUID = 1L;

	public SymbolAlreadyDefinedException() {
		super();
	}

	public SymbolAlreadyDefinedException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public SymbolAlreadyDefinedException(String message, Throwable cause) {
		super(message, cause);
	}

	public SymbolAlreadyDefinedException(String message) {
		super(message);
	}

	public SymbolAlreadyDefinedException(Throwable cause) {
		super(cause);
	}
	
	

}
