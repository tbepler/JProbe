package chiptools.jprobe.function;

import chiptools.Resources;
import jprobe.services.function.Function;

public abstract class AbstractChiptoolsFunction<P> implements Function<P>{
	
	private final Class<P> m_ParamsClass;
	private final String m_Name = Resources.getFunctionName(this.getClass());
	private final String m_Description = Resources.getFunctionDescription(this.getClass());
	private final String m_Category = Resources.getFunctionCategory(this.getClass());
	
	protected AbstractChiptoolsFunction(Class<P> paramsClass){
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
		return m_Category;
	}

	@Override
	public P newParameters() {
		try {
			return m_ParamsClass.newInstance();
		} catch (Exception e){
			throw new RuntimeException(e);
		}
	}

}
