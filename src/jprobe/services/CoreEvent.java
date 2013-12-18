package jprobe.services;

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
	
	public CoreEvent(JProbeCore source, Type type){
		this.source = source;
		this.type = type;
	}
	
	public CoreEvent(JProbeCore source, Type type, Data effected){
		this(source, type);
		dataEffected = effected;
		dataClass = effected.getClass();
	}
	
	public CoreEvent(JProbeCore source, Type type, Class<? extends Data> effected){
		this(source, type);
		dataClass = effected;
	}
	
	public CoreEvent(JProbeCore source, Type type, Function effected){
		this(source, type);
		functionEffected = effected;
	}
	
	public Type type(){
		return type;
	}
	
	public JProbeCore source(){
		return source;
	}
	
	public Data dataChanged(){
		return dataEffected;
	}
	
	public Class<? extends Data> dataClassChanged(){
		return dataClass;
	}
	
	public Function functionChanged(){
		return functionEffected;
	}
	
	
	
}
