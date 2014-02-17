package util.xmlserializer;

/**
 * Exception thrown by SilentObjectCreator
 * 
 * @author Tristan Bepler
 *
 */

public class ObjectInstantiationException extends Exception{

	private static final long serialVersionUID = 1L;
	
	
	public ObjectInstantiationException(){
		super();
	}
	
	public ObjectInstantiationException(Exception e){
		super(e);
	}
	
	public ObjectInstantiationException(String message){
		super(message);
	}
	
	
}
