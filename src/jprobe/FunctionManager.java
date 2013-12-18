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

import jprobe.services.CoreEvent;
import jprobe.services.CoreEvent.Type;
import jprobe.services.CoreListener;
import jprobe.services.Function;
import jprobe.services.JProbeCore;

public class FunctionManager {
	
	private JProbeCore core;
	private Collection<CoreListener> listeners;
	
	private Collection<Function> functions;
	private Map<String, List<Function>> functionsByName;
	
	public FunctionManager(JProbeCore core){
		this.core = core;
		listeners = new HashSet<CoreListener>();
		functions = new PriorityQueue<Function>(10, new Comparator<Function>(){
			@Override
			public int compare(Function arg0, Function arg1) {
				return arg0.getName().compareTo(arg1.getName());
			}
		});
		functionsByName = new HashMap<String, List<Function>>();
	}
	
	public Function[] getAllFunctions(){
		return functions.toArray(new Function[functions.size()]);
	}
	
	public Function[] getFunctions(String name){
		if(functionsByName.containsKey(name)){
			List<Function> list = functionsByName.get(name);
			return list.toArray(new Function[list.size()]);
		}
		return new Function[]{};
	}
	
	public String[] getFunctionNames(){
		return functionsByName.keySet().toArray(new String[functionsByName.size()]);
	}
	
	public void addFunction(Function f, Bundle responsible){
		if(!functions.contains(f)){
			functions.add(f);
			if(!functionsByName.containsKey(f.getName())){
				List<Function> list = new ArrayList<Function>();
				list.add(f);
				functionsByName.put(f.getName(), list);
			}else{
				functionsByName.get(f.getName()).add(f);
			}
			this.notifyListeners(new CoreEvent(core, Type.FUNCTION_ADDED, responsible, f));
		}
	}
	
	public void removeFunction(Function f, Bundle responsible){
		if(functions.remove(f)){
			functionsByName.get(f.getName()).remove(f);
			this.notifyListeners(new CoreEvent(core, Type.FUNCTION_REMOVED, responsible, f));
		}
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
