package util.xmlserializer;

/**
 * Exception thrown by XMLSerializer
 * 
 * @author Tristan Bepler
 *
 */

public class ObjectReadException extends Exception{
	
	private static final long serialVersionUID = 1L;

	public ObjectReadException(){
		super();
	}
	
	public ObjectReadException(Exception e){
		super(e);
	}
	
	public ObjectReadException(String message){
		super(message);
	}
	
}
