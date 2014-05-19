package jprobe.services.function;


public class ExecutionEvent {
	
	public enum Type{
		COMPLETED,
		CANCELLED;
	}
	
	private FunctionExecutor<?> cause;
	private Type type;
	
	public ExecutionEvent(FunctionExecutor<?> cause, Type type){
		this.cause = cause;
		this.type = type;
	}
	
	public Type getType(){
		return type;
	}
	
	public FunctionExecutor<?> getCause(){
		return cause;
	}
	
}
