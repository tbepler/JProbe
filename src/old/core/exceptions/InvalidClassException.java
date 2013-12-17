package old.core.exceptions;

public class InvalidClassException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4672937370686329688L;
	
	
	public InvalidClassException(){
		super();
	}
	
	public InvalidClassException(Exception e){
		super(e);
	}
	
	public InvalidClassException(String message){
		super(message);
	}
}
