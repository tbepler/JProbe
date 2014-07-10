package jprobe.system.model;

import jprobe.framework.model.Value;

public class FixedValue<T> implements Value<T> {
	
	private final T m_Val;
	
	public FixedValue(T val){
		m_Val = val;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Class<? extends T> getType() {
		return (Class<? extends T>) m_Val.getClass();
	}

	@Override
	public T get() throws Exception {
		return m_Val;
	}

}
