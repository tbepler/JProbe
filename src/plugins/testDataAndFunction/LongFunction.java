package plugins.testDataAndFunction;

import org.osgi.framework.Bundle;

import util.progress.ProgressEvent;
import util.progress.ProgressListener;
import jprobe.services.Log;
import jprobe.services.data.Data;
import jprobe.services.data.Field;
import jprobe.services.function.DataParameter;
import jprobe.services.function.FieldParameter;
import jprobe.services.function.Function;

public class LongFunction implements Function{
	
	public static final String DESCRIPTION = "This function takes 10 seconds to complete";
	public static final String NAME = "Long Function";
	
	private static final DataParameter[] DATA_PARAMETERS = new DataParameter[]{};
	private static final FieldParameter[] FIELD_PARAMETERS	= new FieldParameter[]{};
	
	private Bundle m_Bundle;
	
	public LongFunction(Bundle bundle){
		m_Bundle = bundle;
	}
	
	@Override
	public String getName() {
		return LongFunction.NAME;
	}

	@Override
	public String getDescription() {
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
	public Data run(ProgressListener listener, Data[] dataArgs, Field[] fieldArgs) throws Exception {
		int progress = 0;
		Log.getInstance().write(m_Bundle, "Running long function");
		listener.update(new ProgressEvent(this, ProgressEvent.Type.UPDATE, progress, 100));
		while(progress<100){
			Thread.sleep(100);
			listener.update(new ProgressEvent(this, ProgressEvent.Type.UPDATE, ++progress));
			Log.getInstance().write(m_Bundle, "Progress = "+progress);
		}
		return new TestData();
	}

}
