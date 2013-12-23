package plugins.functions.gui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import javax.swing.SwingWorker;

import jprobe.services.Data;
import jprobe.services.DataField;
import jprobe.services.Function;
import jprobe.services.FunctionEvent;
import jprobe.services.FunctionEvent.Type;
import jprobe.services.FunctionParam;
import jprobe.services.FunctionListener;

public class FunctionThread extends SwingWorker implements FunctionListener{
	
	public static final int MAX_PROGRESS = 100;
	public static final String PROGRESS_TAG = "Progress";
	
	private Function function;
	private FunctionParam params;
	private int progressCap;
	private volatile Data result;
	private Collection<FunctionListener> listeners;
	
	public FunctionThread(Function function, FunctionParam params){
		this.function = function;
		this.params = params;
		this.progressCap = this.function.getProgressLength(params);
		this.function.addProgressListener(this);
		this.result = null;
		listeners = new HashSet<FunctionListener>();
	}
	
	public void addProgressListener(FunctionListener listener){
		listeners.add(listener);
	}
	
	public void removeProgressListener(FunctionListener listener){
		listeners.remove(listener);
	}
	
	private void notifyListeners(FunctionEvent event){
		for(FunctionListener l : listeners){
			l.update(event);
		}
	}
	
	@Override
	protected Object doInBackground() throws Exception {
		result = (Data) function.run(params);
		return result;
	}
	
	public Data getResult(){
		return result;
	}

	@Override
	public void update(FunctionEvent event) {
		switch(event.getType()){
		case UPDATE:
			this.setProgress(event.getProgress()*MAX_PROGRESS/progressCap);
			notifyListeners(new FunctionEvent(this, Type.UPDATE, this.getProgress()));
			break;
		}
		this.firePropertyChange(PROGRESS_TAG, old, this.getProgress());
	}
	
	
	
	
	
	
	
	
	
	
}
