package jprobe.system.model.test;

import jprobe.framework.model.Parameter;

public class StubParameter<T> implements Parameter<T> {
	private static final long serialVersionUID = 1L;

	private final Class<T> m_Clazz;
	private final T m_Default;
	private final boolean m_Optional;
	
	public StubParameter(Class<T> clazz, T defaultVal, boolean optional){
		m_Clazz = clazz;
		m_Default = defaultVal;
		m_Optional = optional;
	}
	
	@SuppressWarnings("unchecked")
	public StubParameter(T defaultVal){
		this((Class<T>) defaultVal.getClass(), defaultVal, true);
	}
	
	public StubParameter(Class<T> clazz){
		this(clazz, null, false);
	}
	
	@Override
	public String getName() {
		return "Stub";
	}

	@Override
	public String getDescription() {
		return "Stub parameter";
	}

	@Override
	public String getCategory() {
		return "Stub";
	}

	@Override
	public Character getFlag() {
		return null;
	}

	@Override
	public String getPrototype() {
		return "Stub";
	}

	@Override
	public Class<T> getType() {
		return m_Clazz;
	}

	@Override
	public T getDefaultValue() {
		return m_Default;
	}

	@Override
	public boolean isOptional() {
		return m_Optional;
	}

}
