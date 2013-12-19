package jprobe.services;

import org.osgi.framework.Bundle;

public class CoreEvent {

	public enum Type{
		DATA_ADDED,
		DATA_REMOVED,
		DATA_NAME_CHANGE,
		DATAREADER_ADDED,
		DATAREADER_REMOVED,
		DATAWRITER_ADDED,
		DATAWRITER_REMOVED,
		FUNCTION_ADDED,
		FUNCTION_REMOVED;
	};
	
	private Type type;
	private JProbeCore source;
	private Data dataEffected = null;
	private Class<? extends Data> dataClass = null;
	private Function functionEffected = null;
	private Bundle cause = null;
	
	public CoreEvent(JProbeCore source, Type type, Bundle responsible){
		this.source = source;
		this.type = type;
	}
	
	public CoreEvent(JProbeCore source, Type type, Bundle responsible, Data effected){
		this(source, type, responsible);
		dataEffected = effected;
		dataClass = effected.getClass();
	}
	
	public CoreEvent(JProbeCore source, Type type, Bundle responsible, Class<? extends Data> effected){
		this(source, type, responsible);
		dataClass = effected;
	}
	
	public CoreEvent(JProbeCore source, Type type, Bundle responsible, Function effected){
		this(source, type, responsible);
		functionEffected = effected;
	}
	
	public Type type(){
		return type;
	}
	
	public Bundle getCause(){
		return cause;
	}
	
	public JProbeCore getSource(){
		return source;
	}
	
	public Data getData(){
		return dataEffected;
	}
	
	public Class<? extends Data> getDataClass(){
		return dataClass;
	}
	
	public Function getFunction(){
		return functionEffected;
	}
	
	
	
}
