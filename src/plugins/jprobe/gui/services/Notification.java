package plugins.jprobe.gui.services;

public class Notification {

	private static final long DEFAULT_DURATION = 1500;
	
	public static Notification startEvent(String message){
		return new Notification(message);
	}
	
	public static Notification endEvent(String message, Notification start, long duration){
		return new Notification(message, start, true, duration);
	}
	
	public static Notification endEvent(String message, Notification start){
		return new Notification(message, start, true, DEFAULT_DURATION);
	}
	
	private final String m_Msg;
	private final Notification m_Start;
	private final boolean m_IsEnd;
	private final long m_Duration;
	
	protected Notification(String message, Notification start, boolean isEnd, long duration){
		m_Msg = message;
		m_Start = start;
		m_IsEnd = isEnd;
		m_Duration = duration;
	}
	
	protected Notification(String message){
		this(message, null, false, -1);
	}
	
	public long getDuration(){
		return m_Duration;
	}
	
	public boolean isEnd(){
		return m_IsEnd;
	}
	
	public boolean isStart(){
		return !m_IsEnd;
	}
	
	public String getMessage(){
		return m_Msg;
	}
	
	public Notification getStart(){
		return m_Start;
	}
	
}
