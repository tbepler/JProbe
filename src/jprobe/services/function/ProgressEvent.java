package jprobe.services.function;


public class ProgressEvent {
	
	public enum Type{
		UPDATE,
		CANCELED, 
		COMPLETED;
	}
	
	private final Type m_Type;
	private final Object m_Source;
	private final int m_Progress;
	private final String m_Message;
	private final boolean m_Indeterminant;
	
	public ProgressEvent(Object source, Type type){
		this(source, type, 0);
	}
	
	public ProgressEvent(Object source, Type type, int progress){
		this(source, type, progress, null);
	}
	
	public ProgressEvent(Object source, Type type, int progress, String message){
		this(source, type, progress, message, false);
	}
	
	public ProgressEvent(Object source, Type type, int progress, String message, boolean indeterminant){
		m_Type = type;
		m_Source = source;
		m_Progress = progress;
		m_Message = message;
		m_Indeterminant = indeterminant;
	}
	
	public boolean isIndeterminant(){
		return m_Indeterminant;
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
	
	public Object getSource(){
		return m_Source;
	}
	
}
