package plugins.testDataAndFunction;

import org.osgi.framework.Bundle;

import jprobe.services.data.Data;
import jprobe.services.data.Field;
import jprobe.services.function.DataParameter;
import jprobe.services.function.FieldParameter;
import jprobe.services.function.Function;
import jprobe.services.function.FunctionPrototype;
import jprobe.services.function.InvalidArgumentsException;

public class LongFunctionPrototype implements FunctionPrototype{
	
	private static final DataParameter[] DATA_PARAMETERS = new DataParameter[]{};
	private static final FieldParameter[] FIELD_PARAMETERS	= new FieldParameter[]{};
	
	private Bundle m_Bundle;
	
	public LongFunctionPrototype(Bundle bundle){
		m_Bundle = bundle;
	}
	
	@Override
	public String getFunctionName() {
		return LongFunction.NAME;
	}

	@Override
	public String getFunctionDescription() {
		return LongFunction.DESCRIPTION;
	}

	@Override
	public DataParameter[] getDataParameters() {
		return DATA_PARAMETERS;
	}

	@Override
	public FieldParameter[] getFieldParameters() {
		return FIELD_PARAMETERS;
	}

	@Override
	public Function newInstance(Data[] dataParams,
			Field[] fieldParams) throws InvalidArgumentsException {
		return new LongFunction(m_Bundle);
	}

}
