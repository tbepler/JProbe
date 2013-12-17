package jprobe.services;

public class CoreEvent {

	public enum Type{
		DATA_ADDED,
		DATA_REMOVED,
		DATA_NAME_CHANGE,
		DATAREADER_ADDED,
		DATAREADER_REMOVED,
		DATAWRITER_ADDED,
		DATAWRITER_REMOVED;
	};
	
	private Type type;
	private JProbeCore source;
	private Data dataEffected = null;
	private DataReader readerEffected = null;
	private DataWriter writerEffected = null;
	private Function functionEffected = null;
	
	public CoreEvent(JProbeCore source, Type type){
		this.source = source;
		this.type = type;
	}
	
	public CoreEvent(JProbeCore source, Type type, Data effected){
		this(source, type);
		dataEffected = effected;
	}
	
	public CoreEvent(JProbeCore source, Type type, DataReader effected){
		this(source, type);
		readerEffected = effected;
	}
	
	public CoreEvent(JProbeCore source, Type type, DataWriter effected){
		this(source, type);
		writerEffected = effected;
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
	
	public DataReader readerChanged(){
		return readerEffected;
	}
	
	public DataWriter writerChanged(){
		return writerEffected;
	}
	
	public Function functionChanged(){
		return functionEffected;
	}
	
	
	
}
