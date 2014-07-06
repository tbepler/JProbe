package jprobe.system.model;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;

import jprobe.services.CoreEvent;
import jprobe.services.CoreEvent.Type;
import jprobe.services.function.Function;
import jprobe.services.CoreListener;
import jprobe.services.JProbeCore;

public class FunctionManager{
	
	private final Collection<CoreListener> m_Listeners = new HashSet<CoreListener>();
	private final Collection<Function<?>> m_Functions = new PriorityQueue<Function<?>>(10, new Comparator<Function<?>>(){
		@Override
		public int compare(Function<?> arg0, Function<?> arg1) {
			return arg0.getName().compareTo(arg1.getName());
		}
	});

	private final JProbeCore m_Core;
	
	public FunctionManager(JProbeCore core){
		m_Core = core;
	}
	
	public Collection<Function<?>> getFunctions(){
		return Collections.unmodifiableCollection(m_Functions);
	}
	
	public boolean addFunction(Function<?> f){
		if(!m_Functions.contains(f)){
			m_Functions.add(f);
			this.notifyListeners(new CoreEvent(Type.FUNCTION_ADDED, f));
			return true;
		}
		return false;
	}
	
	public boolean removeFunction(Function<?> f){
		if(m_Functions.remove(f)){
			this.notifyListeners(new CoreEvent(Type.FUNCTION_REMOVED, f));
			return true;
		}
		return false;
	}
	
	private void notifyListeners(CoreEvent event){
		for(CoreListener l : new HashSet<CoreListener>(m_Listeners)){
			l.update(m_Core, event);
		}
	}
	
	public void addListener(CoreListener listener){
		this.m_Listeners.add(listener);
	}
	
	public void removeListener(CoreListener listener){
		this.m_Listeners.remove(listener);
	}
	
	
}
