package plugins.testDataAndFunction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import org.osgi.framework.Bundle;

import jprobe.services.Log;
import jprobe.services.data.Data;
import jprobe.services.function.Function;
import jprobe.services.function.ProgressEvent;
import jprobe.services.function.ProgressListener;

public class LongFunction implements Function{
	
	public static final String DESCRIPTION = "This function takes 10 seconds to complete";
	public static final String NAME = "Long Function";
	
	private Collection<ProgressListener> listeners = new HashSet<ProgressListener>();
	private Bundle m_Bundle;
	
	public LongFunction(Bundle bundle){
		m_Bundle = bundle;
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
		return true;
	}

	@Override
	public int getProgressLength() {
		return 100;
	}

	@Override
	public void addListener(ProgressListener listener) {
		listeners.add(listener);
	}

	@Override
	public void removeListener(ProgressListener listener) {
		listeners.remove(listener);
	}
	
	private void setProgress(int progress){
		for(ProgressListener l : listeners){
			l.update(new ProgressEvent(this, ProgressEvent.Type.UPDATE, progress));
		}
	}

	@Override
	public Data run() throws Exception {
		int progress = 0;
		this.setProgress(progress);
		Log.getInstance().write(m_Bundle, "Running long function");
		while(progress<100){
			Thread.sleep(100);
			this.setProgress(++progress);
			Log.getInstance().write(m_Bundle, "Progress = "+progress);
		}
		return new TestData();
	}

}
