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

import plugins.genome.services.GenomeFunctionPrototype;
import plugins.genome.services.GenomeService;
import plugins.genome.services.GenomeServiceEvent;
import plugins.genome.services.GenomeServiceEvent.Type;
import plugins.genome.services.GenomeServiceListener;

public class GenomeCore implements GenomeService{
	
	public class GenomePrototypeComparator implements Comparator<GenomeFunctionPrototype>, Serializable{
		private static final long serialVersionUID = 1L;

		@Override
		public int compare(GenomeFunctionPrototype o1, GenomeFunctionPrototype o2) {
			return o1.getGenomeFunctionName().compareTo(o2.getGenomeFunctionName());
		}
		
	}
	
	private Queue<GenomeFunctionPrototype> m_Functions;
	private Map<String, List<GenomeFunctionPrototype>> m_FunctionsByName;
	private Collection<GenomeServiceListener> m_Listeners;
	
	public GenomeCore(){
		m_Functions = new PriorityQueue<GenomeFunctionPrototype>(10, new GenomePrototypeComparator());
		m_FunctionsByName = new HashMap<String, List<GenomeFunctionPrototype>>();
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
	public void addGenomeFunctionPrototype(GenomeFunctionPrototype prototype, Bundle adder) {
		m_Functions.add(prototype);
		if(m_FunctionsByName.containsKey(prototype.getGenomeFunctionName())){
			m_FunctionsByName.get(prototype.getGenomeFunctionName()).add(prototype);
		}else{
			List<GenomeFunctionPrototype> list = new ArrayList<GenomeFunctionPrototype>();
			list.add(prototype);
			m_FunctionsByName.put(prototype.getGenomeFunctionName(), list);
		}
		Log.getInstance().write(GenomeActivator.getBundle(), "GenomeFunctionPrototype \""+prototype.getGenomeFunctionName()+
				"\" added by bundle \""+adder.getSymbolicName()+"\"");
		this.notifyListeners(new GenomeServiceEvent(this, Type.FUNCTION_ADDED, prototype, adder));
	}

	@Override
	public void removeGenomeFunctionPrototype(GenomeFunctionPrototype prototype, Bundle remover) {
		if(m_Functions.contains(prototype)){
			m_Functions.remove(prototype);
			m_FunctionsByName.get(prototype.getGenomeFunctionName()).remove(prototype);
			Log.getInstance().write(GenomeActivator.getBundle(), "GenomeFunctionPrototype \""+prototype.getGenomeFunctionName()+
					"\" removed by bundle \""+remover.getSymbolicName()+"\"");
			this.notifyListeners(new GenomeServiceEvent(this, Type.FUNCTION_REMOVED, prototype, remover));
		}
	}

	@Override
	public GenomeFunctionPrototype[] getAllGenomeFunctinoPrototypes() {
		return m_Functions.toArray(new GenomeFunctionPrototype[m_Functions.size()]);
	}

	@Override
	public GenomeFunctionPrototype[] getGenomeFunctionPrototypes(String name) {
		if(m_FunctionsByName.containsKey(name)){
			List<GenomeFunctionPrototype> list = m_FunctionsByName.get(name);
			return list.toArray(new GenomeFunctionPrototype[list.size()]);
		}else{
			return new GenomeFunctionPrototype[]{};
		}
	}

	@Override
	public String[] getGenomeFunctionNames() {
		return m_FunctionsByName.keySet().toArray(new String[m_FunctionsByName.size()]);
	}

}
