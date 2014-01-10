package plugins.functions.gui;

import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.ProgressMonitor;
import javax.swing.SwingWorker;

import org.osgi.framework.Bundle;

import jprobe.services.DataManager;
import jprobe.services.JProbeCore;
import jprobe.services.data.Data;
import jprobe.services.data.DataField;
import jprobe.services.function.Function;
import jprobe.services.function.FunctionEvent;
import jprobe.services.function.FunctionExecutor;
import jprobe.services.function.FunctionListener;
import jprobe.services.function.FunctionParam;

public class SwingFunctionExecutor extends FunctionExecutor implements PropertyChangeListener{
	
	public static final int PROGRESS_BOUND = 100;
	
	class FunctionThread extends SwingWorker<Data, Data> implements FunctionListener{
		
		private Function function;
		private FunctionParam params;
		private int maxProgress = PROGRESS_BOUND;
		
		FunctionThread(Function function, FunctionParam params){
			this.function = function;
			this.params = params;
			this.function.addListener(this);
			maxProgress = function.getProgressLength(params);
		}
		
		@Override
		public void update(FunctionEvent event) {
			switch(event.getType()){
			case UPDATE:
				this.setProgress(event.getProgress()*maxProgress/PROGRESS_BOUND);
				break;
			default:
				break;
			}
		}
		
		@Override
		protected void done(){
			if(this.isCancelled()){
				setCancelled();
				return;
			}
			try{
				setResults(this.get());
				setComplete();
			} catch (Exception ignore){
				
			}
		}

		@Override
		protected Data doInBackground() throws Exception {
			return function.run(params);
		}
		
	}
	
	private boolean completed;
	private boolean cancelled;
	private Bundle bundle;
	private DataManager dataManager;
	private Data result;
	private FunctionThread thread;
	private ProgressWindow monitor;
	//private ProgressMonitor monitor;

	public SwingFunctionExecutor(Function function, FunctionParam params, DataManager dataManager, Bundle bundle) {
		super(function, params);
		this.dataManager = dataManager;
		this.bundle = bundle;
		this.completed = false;
		this.cancelled = false;
		this.result = null;
		this.thread = new FunctionThread(this.getFunction(), this.getParams());
	}
	
	private void setResults(Data data){
		this.result = data;
		dataManager.addData(result, bundle);
	}
	
	private void setComplete(){
		completed = true;
		if(monitor != null){
			monitor.setVisible(false);
		}
	}
	
	private void setCancelled(){
		cancelled = true;
		if(monitor != null){
			monitor.setVisible(false);
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		//monitor.setProgress(thread.getProgress());
		monitor.setValue(thread.getProgress());
	}

	@Override
	public void execute() {
		//this.monitor = new ProgressMonitor(null, thread.function.getName(), null, 0, PROGRESS_BOUND);
		this.monitor = new ProgressWindow(thread.function.getName(), 0, PROGRESS_BOUND, new OnPress(){

			@Override
			public void act() {
				thread.cancel(true);
			}
			
		});
		monitor.setVisible(true);
		thread.addPropertyChangeListener(this);
		thread.execute();
	}

	@Override
	public boolean isComplete() {
		return completed;
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void cancel() {
		if(thread!=null){
			thread.cancel(true);
		}
	}

	@Override
	public Data getResults() {
		return result;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
