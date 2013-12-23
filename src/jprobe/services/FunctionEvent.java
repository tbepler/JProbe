package jprobe.services;

public class FunctionEvent {
	
	public enum Type{
		UPDATE,
		CANCELED, 
		COMPLETED;
	}
	
	private Type type;
	private Object source;
	private int progress;
	
	public FunctionEvent(Object source, Type type){
		this.type = type;
		this.source = source;
		this.progress = 0;
	}
	
	public FunctionEvent(Object source, Type type, int progress){
		this(source, type);
		this.progress = progress;
	}
	
	public int getProgress(){
		return progress;
	}
	
	public Type getType(){
		return type;
	}
	
	public Object getSource(){
		return source;
	}
	
}
