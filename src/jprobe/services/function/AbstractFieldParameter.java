package jprobe.services.function;

import jprobe.services.data.Field;

public abstract class AbstractFieldParameter implements FieldParameter{
	
	private String m_Name;
	private String m_Description;
	private boolean m_Optional;
	private Field m_Type;
	
	protected AbstractFieldParameter(String name, String description, boolean optional, Field type){
		m_Name = name;
		m_Description = description;
		m_Optional = optional;
		m_Type = type;
	}
	
	@Override
	public String getName() {
		return m_Name;
	}

	@Override
	public String getDescription() {
		return m_Description;
	}

	@Override
	public boolean isOptional() {
		return m_Optional;
	}

	@Override
	public Field getType() {
		return m_Type;
	}

}
