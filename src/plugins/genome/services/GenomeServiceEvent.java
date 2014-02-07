package plugins.genome.services;

import org.osgi.framework.Bundle;

public class GenomeServiceEvent {
	
	public enum Type{
		FUNCTION_ADDED,
		FUNCTION_REMOVED;
	}
	
	private Object m_Source;
	private Type m_Type;
	private GenomeFunction m_Function;
	private Bundle m_Responsible;
	
	public GenomeServiceEvent(Object source, Type type, GenomeFunction changed, Bundle responsible){
		m_Source = source;
		m_Type = type;
		m_Function = changed;
		m_Responsible = responsible;
	}
	
	public Object getSource(){
		return m_Source;
	}
	
	public Type getType(){
		return m_Type;
	}
	
	public GenomeFunction getChanged(){
		return m_Function;
	}
	
	public Bundle getBundle(){
		return m_Responsible;
	}
	
}
