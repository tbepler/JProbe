package jprobe.system.model;

import jprobe.framework.model.Parameter;
import jprobe.framework.model.Value;

public class DefaultValue<T> implements Value<T>{
	
	private final Parameter<T> m_Param;
	
	public DefaultValue(Parameter<T> param){
		m_Param = param;
	}
	
	@Override
	public Class<T> getType() {
		return m_Param.getType();
	}

	@Override
	public T get() throws Exception {
		return m_Param.getDefaultValue();
	}

}
