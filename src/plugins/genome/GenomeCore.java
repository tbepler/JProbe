package plugins.genome;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

import jprobe.services.Log;

import org.osgi.framework.Bundle;

import plugins.genome.services.GenomeFunction;
import plugins.genome.services.GenomeService;
import plugins.genome.services.GenomeServiceEvent;
import plugins.genome.services.GenomeServiceEvent.Type;
import plugins.genome.services.GenomeServiceListener;

public class GenomeCore implements GenomeService{
	
	public class GenomeFunctionComparator implements Comparator<GenomeFunction>, Serializable{
		private static final long serialVersionUID = 1L;

		@Override
		public int compare(GenomeFunction o1, GenomeFunction o2) {
			return o1.getFunctionName().compareTo(o2.getFunctionName());
		}
		
	}
	
	private Queue<GenomeFunction> m_Functions;
	private Map<String, List<GenomeFunction>> m_FunctionsByName;
	private Collection<GenomeServiceListener> m_Listeners;
	
	public GenomeCore(){
		m_Functions = new PriorityQueue<GenomeFunction>(10, new GenomeFunctionComparator());
		m_FunctionsByName = new HashMap<String, List<GenomeFunction>>();
		m_Listeners = new HashSet<GenomeServiceListener>();
	}

	@Override
	public void addGenomeServiceListener(GenomeServiceListener l) {
		m_Listeners.add(l);
	}

	@Override
	public void removeGenomeServiceListener(GenomeServiceListener l) {
		m_Listeners.remove(l);
	}
	
	protected void notifyListeners(GenomeServiceEvent event){
		for(GenomeServiceListener l : m_Listeners){
			l.update(event);
		}
	}

	@Override
	public void addGenomeFunction(GenomeFunction prototype, Bundle adder) {
		m_Functions.add(prototype);
		if(m_FunctionsByName.containsKey(prototype.getFunctionName())){
			m_FunctionsByName.get(prototype.getFunctionName()).add(prototype);
		}else{
			List<GenomeFunction> list = new ArrayList<GenomeFunction>();
			list.add(prototype);
			m_FunctionsByName.put(prototype.getFunctionName(), list);
		}
		Log.getInstance().write(GenomeActivator.getBundle(), "GenomeFunctionPrototype \""+prototype.getFunctionName()+
				"\" added by bundle \""+adder.getSymbolicName()+"\"");
		this.notifyListeners(new GenomeServiceEvent(this, Type.FUNCTION_ADDED, prototype, adder));
	}

	@Override
	public void removeGenomeFunction(GenomeFunction function, Bundle remover) {
		if(m_Functions.contains(function)){
			m_Functions.remove(function);
			m_FunctionsByName.get(function.getFunctionName()).remove(function);
			Log.getInstance().write(GenomeActivator.getBundle(), "GenomeFunctionPrototype \""+function.getFunctionName()+
					"\" removed by bundle \""+remover.getSymbolicName()+"\"");
			this.notifyListeners(new GenomeServiceEvent(this, Type.FUNCTION_REMOVED, function, remover));
		}
	}

	@Override
	public GenomeFunction[] getAllGenomeFunctions() {
		return m_Functions.toArray(new GenomeFunction[m_Functions.size()]);
	}

	@Override
	public GenomeFunction[] getGenomeFunctions(String name) {
		if(m_FunctionsByName.containsKey(name)){
			List<GenomeFunction> list = m_FunctionsByName.get(name);
			return list.toArray(new GenomeFunction[list.size()]);
		}else{
			return new GenomeFunction[]{};
		}
	}

	@Override
	public String[] getGenomeFunctionNames() {
		return m_FunctionsByName.keySet().toArray(new String[m_FunctionsByName.size()]);
	}

}
