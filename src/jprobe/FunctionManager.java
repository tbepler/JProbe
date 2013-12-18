package jprobe;

import java.util.Collection;
import java.util.HashSet;

import jprobe.services.CoreEvent;
import jprobe.services.CoreListener;
import jprobe.services.JProbeCore;

public class FunctionManager {
	
	private JProbeCore core;
	private Collection<CoreListener> listeners;
	
	
	public FunctionManager(JProbeCore core){
		this.core = core;
		listeners = new HashSet<CoreListener>();
	}
	
	private void notifyListeners(CoreEvent event){
		for(CoreListener l : listeners){
			l.update(event);
		}
	}
	
	public void addListener(CoreListener listener){
		this.listeners.add(listener);
	}
	
	public void removeListener(CoreListener listener){
		this.listeners.remove(listener);
	}
	
	
}
