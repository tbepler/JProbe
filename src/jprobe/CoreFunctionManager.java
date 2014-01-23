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
import jprobe.services.function.FunctionPrototype;
import jprobe.services.CoreListener;
import jprobe.services.FunctionManager;
import jprobe.services.JProbeCore;

public class CoreFunctionManager implements FunctionManager{
	
	private JProbeCore core;
	private Collection<CoreListener> listeners;
	
	private Collection<FunctionPrototype> functions;
	private Map<String, List<FunctionPrototype>> functionsByName;
	
	public CoreFunctionManager(JProbeCore core){
		this.core = core;
		listeners = new HashSet<CoreListener>();
		functions = new PriorityQueue<FunctionPrototype>(10, new Comparator<FunctionPrototype>(){
			@Override
			public int compare(FunctionPrototype arg0, FunctionPrototype arg1) {
				return arg0.getFunctionName().compareTo(arg1.getFunctionName());
			}
		});
		functionsByName = new HashMap<String, List<FunctionPrototype>>();
	}
	
	@Override
	public FunctionPrototype[] getAllFunctionPrototypes(){
		return functions.toArray(new FunctionPrototype[functions.size()]);
	}
	
	@Override
	public FunctionPrototype[] getFunctionPrototypes(String name){
		if(functionsByName.containsKey(name)){
			List<FunctionPrototype> list = functionsByName.get(name);
			return list.toArray(new FunctionPrototype[list.size()]);
		}
		return new FunctionPrototype[]{};
	}
	
	@Override
	public String[] getFunctionNames(){
		return functionsByName.keySet().toArray(new String[functionsByName.size()]);
	}
	
	@Override
	public void addFunctionPrototype(FunctionPrototype f, Bundle responsible){
		if(!functions.contains(f)){
			functions.add(f);
			if(!functionsByName.containsKey(f.getFunctionName())){
				List<FunctionPrototype> list = new ArrayList<FunctionPrototype>();
				list.add(f);
				functionsByName.put(f.getFunctionName(), list);
			}else{
				functionsByName.get(f.getFunctionName()).add(f);
			}
			this.notifyListeners(new CoreEvent(core, Type.FUNCTION_ADDED, responsible, f));
		}
	}
	
	@Override
	public void removeFunctionPrototype(FunctionPrototype f, Bundle responsible){
		if(functions.remove(f)){
			functionsByName.get(f.getFunctionName()).remove(f);
			this.notifyListeners(new CoreEvent(core, Type.FUNCTION_REMOVED, responsible, f));
		}
	}
	
	private void notifyListeners(CoreEvent event){
		for(CoreListener l : new HashSet<CoreListener>(listeners)){
			l.update(event);
		}
	}
	
	@Override
	public void addListener(CoreListener listener){
		this.listeners.add(listener);
	}
	
	@Override
	public void removeListener(CoreListener listener){
		this.listeners.remove(listener);
	}
	
	
}
