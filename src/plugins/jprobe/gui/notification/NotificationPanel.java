package plugins.jprobe.gui.notification;

import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import plugins.jprobe.gui.ExportImportUtil;
import util.Observer;
import util.Subject;
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
	private final Stack<String> m_Text = new Stack<String>();
	private final Stack<Long> m_Duration = new Stack<Long>();
	private final Observer<ImportEvent> m_ImportObs = new Observer<ImportEvent>(){

		@Override
		public void update(Subject<ImportEvent> observed, ImportEvent notification) {
			NotificationPanel.this.update(notification);
		}
		
	};
	
	private final Observer<ExportEvent> m_ExportObs = new Observer<ExportEvent>(){

		@Override
		public void update(Subject<ExportEvent> observed, ExportEvent notification) {
			NotificationPanel.this.update(notification);
		}
		
	};
	
	public NotificationPanel(JProbeCore core){
		super();
		this.setHorizontalAlignment(JLabel.LEFT);
		this.setFont(this.getFont().deriveFont(FONT_SIZE));
		this.setAlignmentX(JLabel.LEFT_ALIGNMENT);
		m_Core = core;
		m_Core.registerLoad(this);
		m_Core.registerSave(this);
		ExportImportUtil.registerExportObs(m_ExportObs);
		ExportImportUtil.registerImportObs(m_ImportObs);
	}
	
	protected void pushText(String text, long duration){
		m_Text.push(text);
		m_Duration.push(duration);
		this.updateText();
	}
	
	protected void popText(){
		m_Text.pop();
		m_Duration.pop();
		this.updateText();
	}
	
	protected void updateText(){
		if(m_Text.isEmpty()){
			this.setText("");
		}else{
			this.setText(m_Text.peek());
			long duration = m_Duration.peek();
			if(duration > 0){
				this.startTimer(duration);
			}
		}
	}
	
	protected void removeText(String text){
		int index = m_Text.indexOf(text);
		m_Text.remove(index);
		m_Duration.remove(index);
	}
	
	public void dispose(){
		m_Core.unregisterLoad(this);
		m_Core.unregisterSave(this);
		ExportImportUtil.unregisterExportObs(m_ExportObs);
		ExportImportUtil.unregisterImportObs(m_ImportObs);
		if(m_Timer != null){
			m_Timer.cancel();
		}
	}
	
	private void startTimer(long duration){
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
						popText();
					}
					
				});
			}
			
		}, duration);
	}
	
	protected void update(final ExportEvent e){
		SwingUtilities.invokeLater(new Runnable(){

			@Override
			public void run() {
				String start = "Exporting "+e.name+" to file "+e.file;
				switch(e.type){
				case EXPORTED:
					removeText(start);
					pushText("Exported "+e.name+" to file "+e.file, MILLIS_DISPLAY);
					break;
				case EXPORTING:
					pushText(start, 0);
					break;
				case FAILED:
					removeText(start);
					pushText("Failed to export "+e.name+" to file "+e.file, MILLIS_DISPLAY);
					break;
				}
			}
			
		});
	}
	
	protected void update(final ImportEvent e){
		SwingUtilities.invokeLater(new Runnable(){

			@Override
			public void run() {
				String start = "Importing "+e.dataClass.getSimpleName() + " from file "+e.file;
				switch(e.type){
				case FAILED:
					removeText(start);
					pushText("Failed to import "+e.dataClass.getSimpleName() + " from file "+e.file, MILLIS_DISPLAY);
					break;
				case IMPORTED:
					removeText(start);
					pushText("Imported "+e.dataClass.getSimpleName() + " from file "+e.file, MILLIS_DISPLAY);
					break;
				case IMPORTING:
					pushText(start, 0);
					break;
				}
			}
			
		});
	}

	@Override
	public void update(final LoadEvent e) {
		SwingUtilities.invokeLater(new Runnable(){

			@Override
			public void run() {
				String start = "Opening workspace: "+e.file;
				switch(e.type){
				case FAILED:
					removeText(start);
					pushText("Failed to open workspace: "+e.file, MILLIS_DISPLAY);
					break;
				case LOADED:
					removeText(start);
					pushText("Opened workspace: "+e.file, MILLIS_DISPLAY);
					break;
				case LOADING:
					pushText(start, 0);
					break;
				}
			}
			
		});
	}

	@Override
	public void update(final SaveEvent e) {
		SwingUtilities.invokeLater(new Runnable(){

			@Override
			public void run() {
				String start = "Saving workspace to file: "+e.file;
				switch(e.type){
				case FAILED:
					removeText(start);
					pushText("Failed to save file: "+e.file, MILLIS_DISPLAY);
					break;
				case SAVED:
					removeText(start);
					pushText("Saved workspace to file: "+e.file, MILLIS_DISPLAY);
					break;
				case SAVING:
					pushText(start, 0);
					break;
				}
			}
			
		});
		
	}

}
