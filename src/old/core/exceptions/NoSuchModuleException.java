package old.core.exceptions;

public class NoSuchModuleException extends Exception{
	
	public NoSuchModuleException(){
		super();
	}
	
	public NoSuchModuleException(Exception e){
		super(e);
	}
	
	public NoSuchModuleException(String message){
		super(message);
	}
	
}
