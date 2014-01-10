package plugins.testDataAndFunction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import jprobe.services.data.Data;
import jprobe.services.data.DataField;
import jprobe.services.function.Function;
import jprobe.services.function.FunctionEvent;
import jprobe.services.function.FunctionListener;
import jprobe.services.function.FunctionParam;

public class LongFunction implements Function{
	
	private Collection<FunctionListener> listeners = new HashSet<FunctionListener>();
	
	
	@Override
	public String getName() {
		return "Long Function";
	}

	@Override
	public String getDescription() {
		return "This function takes 10 seconds to complete";
	}

	@Override
	public List<Class<? extends Data>> getRequiredDataArgs() {
		return new ArrayList<Class<? extends Data>>();
	}

	@Override
	public List<Class<? extends Data>> getOptionalDataArgs() {
		return new ArrayList<Class<? extends Data>>();
	}

	@Override
	public List<DataField> getRequiredFields() {
		return new ArrayList<DataField>();
	}

	@Override
	public List<DataField> getOptionalFields() {
		return new ArrayList<DataField>();
	}

	@Override
	public boolean isProgressTrackable() {
		return true;
	}

	@Override
	public int getProgressLength(FunctionParam params) {
		return 100;
	}

	@Override
	public void addListener(FunctionListener listener) {
		listeners.add(listener);
	}

	@Override
	public void removeListener(FunctionListener listener) {
		listeners.remove(listener);
	}
	
	private void setProgress(int progress){
		for(FunctionListener l : listeners){
			l.update(new FunctionEvent(this, FunctionEvent.Type.UPDATE, progress));
		}
	}

	@Override
	public Data run(FunctionParam params) throws Exception {
		int progress = 0;
		this.setProgress(progress);
		while(progress<100){
			Thread.sleep(500);
			this.setProgress(++progress);
		}
		return new TestData();
	}

}
