package util.progress;


public class ProgressEvent {
	
	public enum Type{
		UPDATE,
		CANCELED, 
		COMPLETED;
	}
	
	private final Type m_Type;
	private final Object m_Source;
	private final int m_Progress;
	private final int m_MaxProgress;
	private final String m_Message;
	private final boolean m_Indeterminant;
	
	public ProgressEvent(Object source, Type type){
		this(source, type, -1);
	}
	
	public ProgressEvent(Object source, Type type, String message){
		this(source, type, message, true);
	}
	
	public ProgressEvent(Object source, Type type, int progress){
		this(source, type, progress, null);
	}
	
	public ProgressEvent(Object source, Type type, int progress, int maxProgress){
		this(source, type, progress, maxProgress, null);
	}
	
	public ProgressEvent(Object source, Type type, int progress, String message){
		this(source, type, progress, message, false);
	}
	
	public ProgressEvent(Object source, Type type, int progress, int maxProgress, String message){
		this(source, type, progress, maxProgress, message, false);
	}
	
	public ProgressEvent(Object source, Type type, String message, boolean indeterminant){
		this(source, type, 0, 0, message, indeterminant);
	}
	
	public ProgressEvent(Object source, Type type, int progress, String message, boolean indeterminant){
		this(source, type, progress, -1, message, indeterminant);
	}
	
	public ProgressEvent(Object source, Type type, int progress, int maxProgress, String message, boolean indeterminant){
		m_Type = type;
		m_Source = source;
		m_Progress = progress;
		m_MaxProgress = maxProgress;
		m_Message = message;
		m_Indeterminant = indeterminant;
	}
	
	public boolean isIndeterminant(){
		return m_Indeterminant;
	}
	
	public String getMessage(){
		return m_Message;
	}
	
	public int getMaxProgress(){
		return m_MaxProgress;
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
