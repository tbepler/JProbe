package jprobe.services.function;


public class FunctionEvent {
	
	public enum Type{
		UPDATE,
		CANCELED, 
		COMPLETED;
	}
	
	private Type type;
	private Function source;
	private int progress;
	
	public FunctionEvent(Function source, Type type){
		this.type = type;
		this.source = source;
		this.progress = 0;
	}
	
	public FunctionEvent(Function source, Type type, int progress){
		this(source, type);
		this.progress = progress;
	}
	
	public int getProgress(){
		return progress;
	}
	
	public Type getType(){
		return type;
	}
	
	public Function getSource(){
		return source;
	}
	
}
