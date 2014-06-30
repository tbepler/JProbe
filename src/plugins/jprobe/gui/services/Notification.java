package plugins.jprobe.gui.services;

public class Notification {
	
	public static Notification startEvent(String message){
		return new Notification(message);
	}
	
	public static Notification endEvent(String message, Notification start){
		return new Notification(message, start, true);
	}
	
	private final String m_Msg;
	private final Notification m_Start;
	private final boolean m_IsEnd;
	
	protected Notification(String message, Notification start, boolean isEnd){
		m_Msg = message;
		m_Start = start;
		m_IsEnd = isEnd;
	}
	
	protected Notification(String message){
		this(message, null, false);
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
