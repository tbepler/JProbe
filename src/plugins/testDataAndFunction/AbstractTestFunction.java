package plugins.testDataAndFunction;

import jprobe.services.function.Function;

public abstract class AbstractTestFunction<T> implements Function<T>{
	
	public static final String CATEGORY = "Test";
	
	private final String m_Name;
	private final String m_Description;
	private final Class<? extends T> m_ParamsClass;
	
	protected AbstractTestFunction(String name, String description, Class<? extends T> paramsClass){
		m_Name = name;
		m_Description = description;
		m_ParamsClass = paramsClass;
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
	public String getCategory() {
		return CATEGORY;
	}

	@Override
	public T newParameters() {
		try {
			return m_ParamsClass.newInstance();
		} catch (Exception e){
			throw new RuntimeException(e);
		}
	}

}
