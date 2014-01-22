package plugins.testDataAndFunction;

import jprobe.services.data.Data;
import jprobe.services.data.Field;
import jprobe.services.data.IntegerField;
import jprobe.services.data.DoubleField;
import jprobe.services.function.Function;
import jprobe.services.function.ProgressListener;

public class FieldParamFunction implements Function{

	public static final String NAME = "Field Parameter Function";
	public static final String DESCRIPTION = "A function that takes field parameters and returns a test data object with those values";
	
	private String m_String;
	private int m_Int;
	private double m_Double;
	
	public FieldParamFunction(Field[] fieldArgs){
		m_String = fieldArgs[0].asString();
		m_Int = ((IntegerField) fieldArgs[1]).getValue();
		m_Double = ((DoubleField) fieldArgs[2]).getValue();
	}
	
	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public String getDescription() {
		return DESCRIPTION;
	}

	@Override
	public boolean isProgressTrackable() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getProgressLength() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void addListener(ProgressListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeListener(ProgressListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Data run() throws Exception {
		return new TestData(m_String, m_Int, m_Double);
	}
	
	
	
}
