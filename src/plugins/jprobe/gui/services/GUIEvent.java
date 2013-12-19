package plugins.jprobe.gui.services;

import org.osgi.framework.Bundle;

public class GUIEvent {
	
	public enum Type{
		COMPONENT_ADDED,
		COMPONENT_REMOVED,
		MENU_ADDED,
		MENU_REMOVED;
	};
	
	private JProbeGUI source;
	private Bundle cause;
	private Type type;
	
	public GUIEvent(JProbeGUI source, Type type, Bundle cause){
		this.source = source;
		this.type = type;
		this.cause = cause;
	}
	
	public JProbeGUI getSource(){
		return source;
	}
	
	public Bundle getCause(){
		return cause;
	}
	
	public Type getType(){
		return type;
	}
	
}
