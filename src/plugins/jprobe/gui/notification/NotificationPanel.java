package plugins.jprobe.gui.notification;

import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import jprobe.services.JProbeCore;
import jprobe.services.LoadEvent;
import jprobe.services.LoadListener;
import jprobe.services.SaveEvent;
import jprobe.services.SaveListener;

public class NotificationPanel extends JLabel implements SaveListener, LoadListener{
	private static final long serialVersionUID = 1L;
	
	private static final long MILLIS_DISPLAY = 1500;
	
	private static final float FONT_SIZE = 12;
	
	private final JProbeCore m_Core;
	private Timer m_Timer = null;
	
	public NotificationPanel(JProbeCore core){
		super();
		this.setHorizontalAlignment(JLabel.LEFT);
		this.setFont(this.getFont().deriveFont(FONT_SIZE));
		this.setAlignmentX(JLabel.LEFT_ALIGNMENT);
		m_Core = core;
		m_Core.registerLoad(this);
		m_Core.registerSave(this);
		
	}
	
	public void dispose(){
		m_Core.unregisterLoad(this);
		m_Core.unregisterSave(this);
		if(m_Timer != null){
			m_Timer.cancel();
		}
	}
	
	private void startTimer(){
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
						setText("");
					}
					
				});
			}
			
		}, MILLIS_DISPLAY);
	}

	@Override
	public void update(final LoadEvent e) {
		SwingUtilities.invokeLater(new Runnable(){

			@Override
			public void run() {
				switch(e.type){
				case FAILED:
					setText("Failed to open workspace: "+e.file);
					break;
				case LOADED:
					setText("Opened workspace: "+e.file);
					break;
				case LOADING:
					setText("Opening workspace: "+e.file);
					break;
				}
				startTimer();
			}
			
		});
	}

	@Override
	public void update(final SaveEvent e) {
		SwingUtilities.invokeLater(new Runnable(){

			@Override
			public void run() {
				switch(e.type){
				case FAILED:
					setText("Failed to save file: "+e.file);
					break;
				case SAVED:
					setText("Saved workspace to file: "+e.file);
					break;
				case SAVING:
					setText("Saving workspace to file: "+e.file);
					break;
				}
				startTimer();
			}
			
		});
		
	}

}
