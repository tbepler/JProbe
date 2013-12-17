package old.core.exceptions;

public class NotReadableException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public NotReadableException(){
		super();
	}
	
	public NotReadableException(Exception e){
		super(e);
	}
	
	public NotReadableException(String message){
		super(message);
	}
	
}
