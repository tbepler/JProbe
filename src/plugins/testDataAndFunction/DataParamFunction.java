package plugins.testDataAndFunction;

import util.progress.ProgressListener;
import jprobe.services.data.Data;
import jprobe.services.function.Function;

public class DataParamFunction implements Function{

	public static final String NAME = "Data Parameter Function";
	public static final String DESCRIPTION = "A function that requires data parameters to run. It adds all the fields of the given parameters together.";
	
	private Data[] m_DataParams;
	
	public DataParamFunction(Data[] dataParams){
		m_DataParams = dataParams;
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
		String s = "";
		int i = 0;
		double d = 0;
		for(Data data : m_DataParams){
			if(data instanceof TestData){
				TestData tData = (TestData) data;
				s += tData.getString();
				i += tData.getInt();
				d += tData.getDouble();
			}
		}
		return new TestData(s, i, d);
	}

}
