package jprobe;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

import jprobe.services.CoreEvent;
import jprobe.services.CoreEvent.Type;
import jprobe.services.function.Function;
import jprobe.services.AbstractServiceListener;
import jprobe.services.CoreListener;
import jprobe.services.JProbeCore;
import jprobe.services.JProbeLog;

@SuppressWarnings("rawtypes")
public class FunctionManager extends AbstractServiceListener<Function>{
	
	private final Collection<CoreListener> m_Listeners = new HashSet<CoreListener>();
	private final Collection<Function<?>> m_Functions = new PriorityQueue<Function<?>>(10, new Comparator<Function<?>>(){
		@Override
		public int compare(Function<?> arg0, Function<?> arg1) {
			return arg0.getName().compareTo(arg1.getName());
		}
	});

	private final JProbeCore m_Core;
	
	public FunctionManager(JProbeCore core, BundleContext context){
		super(Function.class, context);
		this.m_Core = core;
	}
	
	public Collection<Function<?>> getFunctions(){
		return Collections.unmodifiableCollection(m_Functions);
	}
	
	public void addFunction(Function<?> f, Bundle responsible){
		if(!m_Functions.contains(f)){
			m_Functions.add(f);
			JProbeLog.getInstance().write(responsible, "Function "+f.getName()+" added.");
			this.notifyListeners(new CoreEvent(Type.FUNCTION_ADDED, f));
		}
	}
	
	public void removeFunction(Function<?> f, Bundle responsible){
		if(m_Functions.remove(f)){
			JProbeLog.getInstance().write(responsible, "Function "+f.getName()+" removed.");
			this.notifyListeners(new CoreEvent(Type.FUNCTION_REMOVED, f));
		}
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

	@Override
	public void register(Function service, Bundle provider) {
		this.addFunction(service, provider);
	}

	@Override
	public void unregister(Function service, Bundle provider) {
		this.removeFunction(service, provider);
	}
	
	
}
