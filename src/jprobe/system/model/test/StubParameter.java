package jprobe.system.model.test;

import jprobe.framework.model.Function;
import jprobe.framework.model.Parameter;
import jprobe.system.model.FixedValueFunction;

public class StubParameter<T> extends Parameter<T> {
	private static final long serialVersionUID = 1L;

	private final Function<? extends T> m_Default;
	private final boolean m_Optional;
	
	public StubParameter(Function<? extends T> defaultVal, boolean optional){
		m_Default = defaultVal;
		m_Optional = optional;
	}
	
	public StubParameter(T defaultVal){
		this( new FixedValueFunction<T>(defaultVal), true);
	}
	
	public StubParameter(Class<T> clazz){
		this( new FixedValueFunction<T>(clazz), false);
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
	public Class<? extends T> getReturnType() {
		return m_Default.getReturnType();
	}

	@Override
	public Function<? extends T> getDefaultValue() {
		return m_Default;
	}

	@Override
	public boolean isOptional() {
		return m_Optional;
	}

	@Override
	public Parameter<?>[] getParameters() {
		return m_Default.getParameters();
	}

}
