package old.core.exceptions;

public class FormatNotSupportedException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5866292263377813915L;
	
	public FormatNotSupportedException(){
		super();
	}
	
	public FormatNotSupportedException(Exception e){
		super(e);
	}
	
	public FormatNotSupportedException(String message){
		super(message);
	}
	
	
}
