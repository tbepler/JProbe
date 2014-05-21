package chiptools.jprobe.function;

import chiptools.Constants;
import jprobe.services.function.Function;

public abstract class AbstractChiptoolsFunction<P> implements Function<P>{
	
	private final Class<P> m_ParamsClass;
	
	protected AbstractChiptoolsFunction(Class<P> paramsClass){
		m_ParamsClass = paramsClass;
	}
	
	@Override
	public String getName() {
		return Constants.getName(this.getClass());
	}

	@Override
	public String getDescription() {
		return Constants.getDescription(this.getClass());
	}

	@Override
	public String getCategory() {
		return Constants.getCategory(this.getClass());
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
