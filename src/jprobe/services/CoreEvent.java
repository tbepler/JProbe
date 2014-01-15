package jprobe.services;

import jprobe.services.data.Data;
import jprobe.services.function.Function;
import jprobe.services.function.FunctionPrototype;

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
	
	private Type m_Type;
	private JProbeCore m_Source;
	private Data m_DataEffected = null;
	private Class<? extends Data> m_DataClass = null;
	private FunctionPrototype m_FunctionEffected = null;
	private Bundle m_Cause = null;
	
	public CoreEvent(JProbeCore source, Type type, Bundle responsible){
		this.m_Source = source;
		this.m_Type = type;
	}
	
	public CoreEvent(JProbeCore source, Type type, Bundle responsible, Data effected){
		this(source, type, responsible);
		m_DataEffected = effected;
		m_DataClass = effected.getClass();
	}
	
	public CoreEvent(JProbeCore source, Type type, Bundle responsible, Class<? extends Data> effected){
		this(source, type, responsible);
		m_DataClass = effected;
	}
	
	public CoreEvent(JProbeCore source, Type type, Bundle responsible, FunctionPrototype effected){
		this(source, type, responsible);
		m_FunctionEffected = effected;
	}
	
	public Type type(){
		return m_Type;
	}
	
	public Bundle getCause(){
		return m_Cause;
	}
	
	public JProbeCore getSource(){
		return m_Source;
	}
	
	public Data getData(){
		return m_DataEffected;
	}
	
	public Class<? extends Data> getDataClass(){
		return m_DataClass;
	}
	
	public FunctionPrototype getFunctionPrototype(){
		return m_FunctionEffected;
	}
	
	
	
}
