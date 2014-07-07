package util.progress;


public class ProgressEvent {
	
	public static ProgressEvent newProgressUpdate(Object source, int progress, int maxProgress){
		return new ProgressEvent(source, Type.PROGRESS_UPDATE, progress, maxProgress, null, null, null);
	}
	
	public static ProgressEvent newMessageUpdate(Object source, String message){
		return new ProgressEvent(source, Type.MESSAGE_UPDATE, -1, -1, message, null, null);
	}
	
	public static ProgressEvent newIndeterminantUpdate(Object source, boolean indeterminant){
		return new ProgressEvent(source, Type.INDETERMINANT_UPDATE, -1, -1, null, indeterminant, null);
	}
	
	public static ProgressEvent newErrorEvent(Object source, Throwable t){
		return new ProgressEvent(source, Type.ERROR, -1, -1, null, null, t);
	}
	
	public static ProgressEvent newCanceledEvent(Object source){
		return new ProgressEvent(source, Type.CANCELED, -1, -1, null, null, null);
	}
	
	public static ProgressEvent newCompletedEvent(Object source){
		return new ProgressEvent(source, Type.COMPLETED, -1, -1, null, null, null);
	}
	
	public enum Type{
		PROGRESS_UPDATE,
		MESSAGE_UPDATE,
		INDETERMINANT_UPDATE,
		ERROR,
		CANCELED, 
		COMPLETED;
	}
	
	private final Type m_Type;
	private final Object m_Source;
	private final int m_Progress;
	private final int m_MaxProgress;
	private final String m_Message;
	private final Boolean m_Indeterminant;
	private final Throwable m_Throwable;
	
	private ProgressEvent(Object source, Type type, int progress, int maxProgress, String message, Boolean indeterminant, Throwable t){
		m_Type = type;
		m_Source = source;
		m_Progress = progress;
		m_MaxProgress = maxProgress;
		m_Message = message;
		m_Indeterminant = indeterminant;
		m_Throwable = t;
	}
	
	public Throwable getThrowable(){
		return m_Throwable;
	}
	
	public Boolean isIndeterminant(){
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
