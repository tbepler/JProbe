package old.core.exceptions;

public class CoreInitializationException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public CoreInitializationException(){
		super();
	}
	
	public CoreInitializationException(Exception e){
		super(e);
	}
	
	public CoreInitializationException(String message){
		super(message);
	}
	
	
}
