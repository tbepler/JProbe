package jprobe;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

import jprobe.services.CoreEvent;
import jprobe.services.CoreEvent.Type;
import jprobe.services.function.Function;
import jprobe.services.AbstractServiceListener;
import jprobe.services.CoreListener;
import jprobe.services.FunctionManager;
import jprobe.services.JProbeCore;

@SuppressWarnings("rawtypes")
public class CoreFunctionManager extends AbstractServiceListener<Function> implements FunctionManager{
	
	private JProbeCore m_Core;
	private Collection<CoreListener> m_Listeners;
	
	private Map<Function<?>, Bundle> m_Providers;
	private Collection<Function<?>> m_Functions;
	private Map<String, List<Function<?>>> m_FunctionsByName;
	
	public CoreFunctionManager(JProbeCore core, BundleContext context){
		super(Function.class, context);
		this.m_Core = core;
		m_Listeners = new HashSet<CoreListener>();
		m_Providers = new HashMap<Function<?>, Bundle>();
		m_Functions = new PriorityQueue<Function<?>>(10, new Comparator<Function<?>>(){
			@Override
			public int compare(Function<?> arg0, Function<?> arg1) {
				return arg0.getName().compareTo(arg1.getName());
			}
		});
		m_FunctionsByName = new HashMap<String, List<Function<?>>>();
	}
	
	@Override
	public Function<?>[] getAllFunctions(){
		return m_Functions.toArray(new Function<?>[m_Functions.size()]);
	}
	
	@Override
	public Function<?>[] getFunctions(String name){
		if(m_FunctionsByName.containsKey(name)){
			List<Function<?>> list = m_FunctionsByName.get(name);
			return list.toArray(new Function<?>[list.size()]);
		}
		return new Function[]{};
	}
	
	@Override
	public String[] getFunctionNames(){
		return m_FunctionsByName.keySet().toArray(new String[m_FunctionsByName.size()]);
	}
	
	@Override
	public void addFunction(Function<?> f, Bundle responsible){
		if(!m_Functions.contains(f)){
			m_Functions.add(f);
			m_Providers.put(f, responsible);
			if(!m_FunctionsByName.containsKey(f.getName())){
				List<Function<?>> list = new ArrayList<Function<?>>();
				list.add(f);
				m_FunctionsByName.put(f.getName(), list);
			}else{
				m_FunctionsByName.get(f.getName()).add(f);
			}
			this.notifyListeners(new CoreEvent(m_Core, Type.FUNCTION_ADDED, responsible, f));
		}
	}
	
	@Override
	public void removeFunction(Function<?> f, Bundle responsible){
		if(m_Functions.remove(f)){
			m_Providers.remove(f);
			m_FunctionsByName.get(f.getName()).remove(f);
			this.notifyListeners(new CoreEvent(m_Core, Type.FUNCTION_REMOVED, responsible, f));
		}
	}
	
	@Override
	public Bundle getProvider(Function<?> f){
		return m_Providers.get(f);
	}
	
	private void notifyListeners(CoreEvent event){
		for(CoreListener l : new HashSet<CoreListener>(m_Listeners)){
			l.update(event);
		}
	}
	
	@Override
	public void addListener(CoreListener listener){
		this.m_Listeners.add(listener);
	}
	
	@Override
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
