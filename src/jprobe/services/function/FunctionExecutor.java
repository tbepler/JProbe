package jprobe.services.function;

import java.util.Collection;
import java.util.HashSet;

import jprobe.services.data.Data;


public abstract class FunctionExecutor {

	private Function m_Function;
	private Collection<ExecutionListener> m_Listeners;
	
	public FunctionExecutor(Function function){
		this.m_Function = function;
		m_Listeners = new HashSet<ExecutionListener>();
	}
	
	public abstract void execute();
	public abstract boolean isComplete();
	public abstract boolean isCancelled();
	public abstract void cancel();
	public abstract Data getResults();
	
	public void addListener(ExecutionListener listener){
		m_Listeners.add(listener);
	}
	
	public void removeListener(ExecutionListener listener){
		m_Listeners.remove(listener);
	}
	
	protected void notifyListeners(ExecutionEvent event){
		for(ExecutionListener l : m_Listeners){
			l.update(event);
		}
	}
	
	protected Function getFunction(){
		return m_Function;
	}

}