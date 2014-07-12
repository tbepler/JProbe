package jprobe.system.model.test;

import java.util.List;

import jprobe.framework.model.function.Function;
import jprobe.framework.model.function.Parameter;
import jprobe.framework.model.types.Signature;
import jprobe.system.model.FixedValueFunction;

public class ListParameter<T> extends Parameter<List<T>> {
	private static final long serialVersionUID = 1L;
	
	private final Class<? extends T> m_Clazz;
	private final List<T> m_Default;
	private final boolean m_Optional;
	
	private ListParameter(Class<? extends T> clazz, List<T> defaultVal, boolean optional){
		m_Clazz = clazz;
		m_Default = defaultVal;
		m_Optional = optional;
	}
	
	public ListParameter(Class<? extends T> clazz){
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
	public Class<? extends List<T>> getReturnType() {
		return (Class<? extends List<T>>) List.class;
	}

	@Override
	public Function<List<T>> getDefaultValue() {
		if(m_Default != null){
			return new FixedValueFunction<List<T>>(m_Default);
		}
		return null;
	}

	@Override
	public boolean isOptional() {
		return m_Optional;
	}

	@Override
	public Signature<?>[] getParameters() {
		return new Signature<?>[]{};
	}

}
