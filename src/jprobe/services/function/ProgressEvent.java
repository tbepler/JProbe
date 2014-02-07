package jprobe.services.function;


public class ProgressEvent {
	
	public enum Type{
		UPDATE,
		CANCELED, 
		COMPLETED;
	}
	
	private final Type m_Type;
	private final Function m_Source;
	private final int m_Progress;
	private final String m_Message;
	
	public ProgressEvent(Function source, Type type){
		this(source, type, 0);
	}
	
	public ProgressEvent(Function source, Type type, int progress){
		this(source, type, progress, null);
	}
	
	public ProgressEvent(Function source, Type type, int progress, String message){
		m_Type = type;
		m_Source = source;
		m_Progress = progress;
		m_Message = message;
	}
	
	public String getMessage(){
		return m_Message;
	}
	
	public int getProgress(){
		return m_Progress;
	}
	
	public Type getType(){
		return m_Type;
	}
	
	public Function getSource(){
		return m_Source;
	}
	
}
