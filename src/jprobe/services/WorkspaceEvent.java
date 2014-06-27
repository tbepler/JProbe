package jprobe.services;

import jprobe.services.data.Data;

public class WorkspaceEvent {
	
	public enum Type{
		DATA_ADDED,
		DATA_REMOVED,
		DATA_RENAMED,
		WORKSPACE_CLEARED,
		WORKSPACE_LOADED,
		WORKSPACE_IMPORTED,
		WORKSPACE_RENAMED;
	}
	
	public final Type type;
	public final Data data;
	
	public WorkspaceEvent(Type type, Data data){
		this.type = type;
		this.data = data;
	}
	
	public WorkspaceEvent(Type type){
		this(type, null);
	}
	
}
