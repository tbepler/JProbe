package plugins.jprobe.gui.notification;

import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import plugins.jprobe.gui.services.Notification;

public class NotificationPanel extends JLabel{
	private static final long serialVersionUID = 1L;
	
	private static final float FONT_SIZE = 12;
	
	private Timer m_Timer = null;
	private final Stack<Notification> m_Notes = new Stack<Notification>();
	
	public NotificationPanel(){
		super();
		this.setHorizontalAlignment(JLabel.LEFT);
		this.setFont(this.getFont().deriveFont(FONT_SIZE));
		this.setAlignmentX(JLabel.LEFT_ALIGNMENT);
	}
	
	public synchronized void pushNotification(Notification n){
		if(n.isEnd()){
			m_Notes.remove(n.getStart());
		}
		m_Notes.push(n);
		
	}
	
	protected synchronized void removeNotification(Notification n){
		if(n == this.getCurNotification()){
			m_Notes.pop();
			this.updateText();
		}else{
			m_Notes.remove(n);
		}
	}
	
	protected Notification getCurNotification(){
		if(!m_Notes.isEmpty()){
			return m_Notes.peek();
		}
		return null;
	}
	
	protected void updateText(){
		if(m_Notes.isEmpty()){
			this.setText("");
		}else{
			Notification n = m_Notes.peek();
			this.setText(n.getMessage());
			long duration = n.getDuration();
			if(duration > 0){
				this.startTimer(n);
			}
		}
	}
	
	private void startTimer( final Notification n){
		if(m_Timer != null){
			m_Timer.cancel();
		}
		m_Timer = new Timer();
		m_Timer.schedule(new TimerTask(){

			@Override
			public void run() {
				SwingUtilities.invokeLater(new Runnable(){

					@Override
					public void run() {
						removeNotification(n);
					}
					
				});
			}
			
		}, n.getDuration());
	}


}
