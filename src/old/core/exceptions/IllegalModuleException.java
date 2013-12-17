package old.core.exceptions;

public class IllegalModuleException extends Exception{

	private static final long serialVersionUID = -6979300457407097443L;
	
	public IllegalModuleException(){
		super();
	}
	
	public IllegalModuleException(Exception e){
		super(e);
	}
	
	public IllegalModuleException(String message){
		super(message);
	}

}
