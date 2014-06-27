package jprobe.services;

import jprobe.services.data.DataReader;
import jprobe.services.data.DataWriter;
import jprobe.services.function.Function;

public class CoreEvent {

	public enum Type{
		DATAREADER_ADDED,
		DATAREADER_REMOVED,
		DATAWRITER_ADDED,
		DATAWRITER_REMOVED,
		FUNCTION_ADDED,
		FUNCTION_REMOVED,
		WORKSPACE_NEW,
		WORKSPACE_CLOSED;
	};
	
	public final Type type;
	public final DataReader reader;
	public final DataWriter writer;
	public final Function<?> function;
	public final Workspace workspace;
	
	private CoreEvent(Type type, DataReader reader, DataWriter writer, Function<?> function, Workspace workspace){
		this.type = type;
		this.reader = reader;
		this.writer = writer;
		this.function = function;
		this.workspace = workspace;
	}
	
	public CoreEvent(Type type, DataReader reader){
		this(type, reader, null, null, null);
	}
	
	public CoreEvent(Type type, DataWriter writer){
		this(type, null, writer, null, null);
	}
	
	public CoreEvent(Type type, Function<?> function){
		this(type, null, null, function, null);
	}
	
	public CoreEvent(Type type, Workspace workspace){
		this(type , null, null, null, workspace);
	}

}
