package jprobe.services.function;


public class ProgressEvent {
	
	public enum Type{
		UPDATE,
		CANCELED, 
		COMPLETED;
	}
	
	private Type type;
	private Function source;
	private int progress;
	
	public ProgressEvent(Function source, Type type){
		this.type = type;
		this.source = source;
		this.progress = 0;
	}
	
	public ProgressEvent(Function source, Type type, int progress){
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
