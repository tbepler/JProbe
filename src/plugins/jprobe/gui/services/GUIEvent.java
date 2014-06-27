package plugins.jprobe.gui.services;

import org.osgi.framework.Bundle;

public class GUIEvent {
	
	public enum Type{
		COMPONENT_ADDED,
		COMPONENT_REMOVED,
		MENU_ADDED,
		MENU_REMOVED;
	};
	
	private final JProbeWindow source;
	private final Bundle cause;
	private final Type type;
	
	public GUIEvent(JProbeWindow source, Type type, Bundle cause){
		this.source = source;
		this.type = type;
		this.cause = cause;
	}
	
	public JProbeWindow getSource(){
		return source;
	}
	
	public Bundle getCause(){
		return cause;
	}
	
	public Type getType(){
		return type;
	}
	
}
