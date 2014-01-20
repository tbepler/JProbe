package jprobe.services.function;

import jprobe.services.data.Data;

public abstract class AbstractDataParameter implements DataParameter{
	
	private final String m_Name;
	private final String m_Description;
	private final boolean m_Optional;
	private final Class<? extends Data> m_Type;
	
	protected AbstractDataParameter(String name, String description, boolean optional, Class<? extends Data> type){
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
	public Class<? extends Data> getType() {
		return m_Type;
	}

}
