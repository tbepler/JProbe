package jprobe.services;

import jprobe.services.data.Data;
import jprobe.services.function.Function;

public class CoreEvent {

	public enum Type{
		DATA_READABLE,
		DATA_UNREADABLE,
		DATA_WRITABLE,
		DATA_UNWRITABLE,
		FUNCTION_ADDED,
		FUNCTION_REMOVED,
		WORKSPACE_NEW,
		WORKSPACE_CLOSED;
	};
	
	public final Type type;
	public final Class<? extends Data> clazz;
	public final Function<?> function;
	public final Workspace workspace;
	
	private CoreEvent(Type type, Class<? extends Data> clazz, Function<?> function, Workspace workspace){
		this.type = type;
		this.clazz=  clazz;
		this.function = function;
		this.workspace = workspace;
	}
	
	public CoreEvent(Type type, Class<? extends Data> changed){
		this(type, changed, null, null);
	}
	
	public CoreEvent(Type type, Function<?> function){
		this(type, null, function, null);
	}
	
	public CoreEvent(Type type, Workspace workspace){
		this(type , null, null, workspace);
	}

}
