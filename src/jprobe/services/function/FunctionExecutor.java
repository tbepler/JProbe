package jprobe.services.function;

import java.util.Collection;
import java.util.HashSet;

import jprobe.services.data.Data;


public abstract class FunctionExecutor {

	private Function function;
	private FunctionParam params;
	private Collection<ExecutionListener> listeners;
	
	public FunctionExecutor(Function function, FunctionParam params){
		this.function = function;
		this.params = params;
		listeners = new HashSet<ExecutionListener>();
	}
	
	public abstract void execute();
	public abstract boolean isComplete();
	public abstract boolean isCancelled();
	public abstract void cancel();
	public abstract Data getResults();
	
	public void addListener(ExecutionListener listener){
		listeners.add(listener);
	}
	
	public void removeListener(ExecutionListener listener){
		listeners.remove(listener);
	}
	
	protected void notifyListeners(ExecutionEvent event){
		for(ExecutionListener l : listeners){
			l.update(event);
		}
	}
	
	protected FunctionParam getParams(){
		return params;
	}
	
	protected Function getFunction(){
		return function;
	}

}