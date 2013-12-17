package old.core.exceptions;

public class IllegalStringException extends Exception{

	/**
	 * Serial ID.
	 */
	private static final long serialVersionUID = -4954590716049766045L;

	public IllegalStringException(){
		super();
	}
	public IllegalStringException(Exception e){
		super(e);
	}
	public IllegalStringException(String message){
		super(message);
	}
	
}
