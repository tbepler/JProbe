package old.core.exceptions;

public class FileReadException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 660879628863921313L;

	public FileReadException(){
		super();
	}
	
	public FileReadException(Exception e){
		super(e);
	}
	
	public FileReadException(String message){
		super(message);
	}
	
}
