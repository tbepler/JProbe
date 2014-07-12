package jprobe.system.model.test;

import jprobe.framework.model.function.Function;
import jprobe.framework.model.function.Parameter;
import jprobe.framework.model.types.Signature;
import jprobe.system.model.FixedValueFunction;

public class StubParameter<T> extends Parameter<T> {
	private static final long serialVersionUID = 1L;

	private final Function<? extends T> m_Default;
	private final Signature<?>[] m_Signatures; 
	private final Class<? extends T> m_Clazz;
	private final boolean m_Optional;
	
	public StubParameter(Function<? extends T> defaultVal, boolean optional){
		m_Default = defaultVal;
		m_Clazz = m_Default.getReturnType();
		m_Signatures = m_Default.getParameters();
		m_Optional = optional;
	}
	
	public StubParameter(T defaultVal){
		this( new FixedValueFunction<T>(defaultVal), true);
	}
	
	public StubParameter(Class<? extends T> clazz){
		this( new FixedValueFunction<T>(clazz), false);
	}
	
	public StubParameter(Class<? extends T> clazz, Signature<?>[] signature){
		m_Default = null;
		m_Clazz = clazz;
		m_Signatures = signature;
		m_Optional = false;
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
		return m_Clazz;
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
	public Signature<?>[] getParameters() {
		return m_Signatures;
	}

}
