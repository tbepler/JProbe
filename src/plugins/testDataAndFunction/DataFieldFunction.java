package plugins.testDataAndFunction;

import util.progress.ProgressListener;
import jprobe.services.data.Data;
import jprobe.services.function.Function;

public class DataFieldFunction implements Function{
	
	public static final String NAME = "Data and Fields Function";
	public static final String DESCRIPTION = "A function that requires both data and field parameters";
	
	private TestData m_Data;
	private String m_String;
	private int m_Int;
	private double m_Double;
	
	public DataFieldFunction(TestData data, String s, int i, double d){
		m_Data = data;
		m_String = s;
		m_Int = i;
		m_Double = d;
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
		return new TestData(m_Data.getString()+m_String, m_Data.getInt()+m_Int, m_Data.getDouble()+m_Double);
	}

}
