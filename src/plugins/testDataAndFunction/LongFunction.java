package plugins.testDataAndFunction;

import java.util.ArrayList;
import java.util.Collection;

import org.osgi.framework.Bundle;

import plugins.testDataAndFunction.params.NullParameter;
import util.progress.ProgressEvent;
import util.progress.ProgressListener;
import jprobe.services.Log;
import jprobe.services.data.Data;
import jprobe.services.function.Argument;

public class LongFunction extends AbstractTestFunction<NullParameter>{
	
	public static final String DESCRIPTION = "This function takes 10 seconds to complete";
	public static final String NAME = "long";
	
	private final Bundle m_Bundle = Activator.getBundle();
	
	public LongFunction(Bundle bundle){
		super(NAME, DESCRIPTION, NullParameter.class);
	}

	@Override
	public Collection<Argument<? super NullParameter>> getArguments() {
		return new ArrayList<Argument<? super NullParameter>>();
	}

	@Override
	public Data execute(ProgressListener listener, NullParameter params) throws Exception {
		int progress = 0;
		Log.getInstance().write(m_Bundle, "Running long function");
		listener.update(new ProgressEvent(this, ProgressEvent.Type.UPDATE, progress, 100));
		while(progress<100){
			Thread.sleep(100);
			listener.update(new ProgressEvent(this, ProgressEvent.Type.UPDATE, ++progress, 100));
			Log.getInstance().write(m_Bundle, "Progress = "+progress);
		}
		return new TestData();
	}

}
