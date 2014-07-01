package plugins.jprobe.gui;

import java.util.HashMap;
import java.util.Map;

import plugins.jprobe.gui.services.Notification;
import util.save.SaveableEvent;

public class NotificationProducer {
	
	private final Map<SaveableEvent, Notification> m_EventMap = new HashMap<SaveableEvent, Notification>();
	
	public Notification toNotification(SaveableEvent event){
		switch(event.type){
		case FAILED:
			return this.notifyFailed(event);
		case IMPORTED:
			return this.notifyEndImport(event);
		case IMPORTING:
			return this.notifyStartImport(event);
		case LOADED:
			return this.notifyEndLoad(event);
		case LOADING:
			return this.notifyStartLoad(event);
		case SAVED:
			return this.notifyEndSave(event);
		case SAVING:
			return this.notifyStartSave(event);
		default:
			return null;
		}
	}
	
	protected Notification notifyEndSave(SaveableEvent event){
		SaveableEvent prev = event.start;
		Notification start = m_EventMap.get(prev);
		Notification end = Notification.endEvent(Constants.SAVE_WORKSPACE_SUCCESS + event.target, start);
		m_EventMap.remove(prev);
		return end;
	}
	
	protected Notification notifyEndLoad(SaveableEvent event){
		SaveableEvent prev = event.start;
		Notification start = m_EventMap.get(prev);
		Notification end = Notification.endEvent(Constants.LOAD_WORKSPACE_SUCCESS + event.target, start);
		m_EventMap.remove(prev);
		return end;
	}
	
	protected Notification notifyEndImport(SaveableEvent event){
		SaveableEvent prev = event.start;
		Notification start = m_EventMap.get(prev);
		Notification end = Notification.endEvent(Constants.IMPORT_WORKSPACE_SUCCESS + event.target, start);
		m_EventMap.remove(prev);
		return end;
	}
	
	protected Notification notifyStartSave(SaveableEvent event){
		Notification start = Notification.startEvent(Constants.SAVE_WORKSPACE_START + event.target);
		m_EventMap.put(event, start);
		return start;
	}
	
	protected Notification notifyStartLoad(SaveableEvent event){
		Notification start = Notification.startEvent(Constants.LOAD_WORKSPACE_START + event.target);
		m_EventMap.put(event, start);
		return start;
	}
	
	protected Notification notifyStartImport(SaveableEvent event){
		Notification start = Notification.startEvent(Constants.IMPORT_WORKSPACE_START + event.target);
		m_EventMap.put(event, start);
		return start;
	}
	
	protected Notification notifyFailed(SaveableEvent event){
		SaveableEvent start = event.start;
		Notification prev = m_EventMap.get(start);
		String target = event.target;
		String message;
		switch(start.type){
		case IMPORTING:
			message = Constants.IMPORT_WORKSPACE_ERROR + target;
			break;
		case LOADING:
			message = Constants.LOAD_WORKSPACE_ERROR + target;
			break;
		case SAVING:
			message = Constants.SAVE_WORKSPACE_ERROR + target;
			break;
		default:
			return null;
		}
		Notification failedNote = Notification.endEvent(message, prev);
		m_EventMap.remove(start);
		return failedNote;
	}
	
}
